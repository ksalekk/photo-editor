package controller;

import com.sun.jdi.InvalidTypeException;
import utils.IOManager;
import model.ImageModel;
import imageprocessing.ImageProcessing;
import views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;

/**
 * AppController plays the Controller role in M(VC)-architecture.
 * It handles requests received from user by MainView and return responses.
 */
public class AppController {
    private ImageModel imageModel;
    private MainView mainView;

    private final Stack<BufferedImage> undoStack;
    private final Stack<BufferedImage> redoStack;

    public AppController() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void setView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public void loadImage() {
        String filename = mainView.getImageSourceFromUser();
        if (filename == null) {
            return;
        }

        BufferedImage input = null;
        try {
            input = IOManager.loadImage(filename);
        } catch (InvalidTypeException e) {
            displayWarning(
                    "Invalid Image Type",
                    e.getMessage()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if(input != null) {
            addToUndoStack(imageModel.getProcessedImage());
            imageModel.setProcessedImage(input);
            mainView.setViewMode(true);
            mainView.enableUndo(true);

            mainView.displayImage(input);
            if (((JFrame) mainView).getExtendedState() != Frame.MAXIMIZED_BOTH) {
                ((JFrame) mainView).pack();
            }
        }
    }

    public void saveImage() {
        String dirname = mainView.getImageDestinationFromUser();
        if(dirname == null) {
            return;
        }
        IOManager.saveImage(imageModel.getProcessedImage(), dirname, "jpg");
    }


    public void handleUndo() {
        if(undoStack.empty()) {
            return;
        }

        redoStack.push(imageModel.getProcessedImage());
        mainView.enableRedo(true);

        BufferedImage prev = undoStack.pop();
        if(undoStack.empty()) {
            mainView.enableUndo(false);
        }

        imageModel.setProcessedImage(prev);
        mainView.displayImage(prev);
    }

    public void handleRedo() {
        if(redoStack.empty()) {
            return;
        }

        undoStack.push(imageModel.getProcessedImage());
        mainView.enableUndo(true);

        BufferedImage next = redoStack.pop();
        if(redoStack.empty()) {
            mainView.enableRedo(false);
        }

        imageModel.setProcessedImage(next);
        mainView.displayImage(next);
    }

    private void addToUndoStack(BufferedImage prevImage) {
        undoStack.push(prevImage);

        mainView.enableRedo(false);
        redoStack.clear();
    }

    public void toGrayScale() {
        BufferedImage input = imageModel.getProcessedImage();
        if(input.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return;
        }

        addToUndoStack(input);
        BufferedImage resultImage = ImageProcessing.RGBtoGray(input);
        imageModel.setProcessedImage(resultImage);
        mainView.displayImage(resultImage);
    }

    public void handleFiltration(float[] kernelElements) {
        BufferedImage input = imageModel.getProcessedImage();
        addToUndoStack(input);
        BufferedImage resultImage = ImageProcessing.linearFiltration(input, kernelElements);
        imageModel.setProcessedImage(resultImage);
        mainView.displayImage(resultImage);
    }

    public void handleBrightness(int offset) {
        BufferedImage input = imageModel.getProcessedImage();
        imageModel.displayedImageOffset = offset;
        BufferedImage resultImage = ImageProcessing.colorAdjustment(
                input,
                imageModel.displayedImageOffset,
                imageModel.displayedImageScale);

        imageModel.setDisplayedImage(resultImage);
        mainView.displayImage(resultImage);
    }

    public void handleContrast(float scale) {
        BufferedImage input = imageModel.getProcessedImage();
        imageModel.displayedImageScale = scale;
        BufferedImage resultImage = ImageProcessing.colorAdjustment(
                input,
                imageModel.displayedImageOffset,
                imageModel.displayedImageScale
        );
        imageModel.setDisplayedImage(resultImage);
        mainView.displayImage(resultImage);
    }

    public void applyChanges() {
        imageModel.displayedImageScale = 1f;
        imageModel.displayedImageOffset = 0;
        addToUndoStack(imageModel.getProcessedImage());
        imageModel.setProcessedImage(imageModel.getDisplayedImage());
    }

    public void closedDialogWindow() {
        imageModel.displayedImageScale = 1f;
        imageModel.displayedImageOffset = 0;
        mainView.displayImage(imageModel.getProcessedImage());
    }

    public void exit() {
        System.exit(0);
    }

    private void displayWarning(String title, String message) {
        JOptionPane.showMessageDialog(
                new JFrame(),
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
}
