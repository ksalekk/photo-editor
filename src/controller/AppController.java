package controller;

import com.sun.jdi.InvalidTypeException;
import utils.IOManager;
import model.ImageModel;
import utils.ImageProcessing;
import views.MainView;

import javax.swing.*;
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

    /** Get image absolute path from user and load this image */
    public void handleLoadImage() {
        String filename = mainView.getImageSourceFromUser();
        if (filename == null) {
            return;
        }

        BufferedImage input = null;
        try {
            input = IOManager.loadImage(filename);
        } catch (InvalidTypeException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid Image Type", e.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if(input != null) {
            addToUndoStack(imageModel.getProcessedImage());
            imageModel.setOriginalImage(input);
            mainView.setViewMode(true);
            mainView.enableUndo(true);

            mainView.displayImage(input);
        }
    }


    /** Get absolute path from user and save there processed image (as jpg) */
    public void handleSaveImage() {
        String dirname = mainView.getImageDestinationFromUser();
        if(dirname == null) {
            return;
        }
        IOManager.saveImage(imageModel.getProcessedImage(), dirname, "jpg");
    }

    /** Push current processed image to the undo stack, pull image from the redo stack */
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

    /** Push current processed image to the redo stack, pull image from the undo stack */
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


    /** Convert current processed image to the gray scale */
    public void handleToGrayScale() {
        BufferedImage input = imageModel.getProcessedImage();
        if(input.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return;
        }

        addToUndoStack(input);
        BufferedImage resultImage = ImageProcessing.RGBtoGray(input);
        imageModel.setProcessedImage(resultImage);
        mainView.displayImage(resultImage);
    }

    /** Filter processed image with the specified kernel */
    public void handleFiltration(float[] kernelElements) {
        BufferedImage input = imageModel.getProcessedImage();
        addToUndoStack(input);
        BufferedImage resultImage = ImageProcessing.linearFiltration(input, kernelElements);
        imageModel.setProcessedImage(resultImage);
        mainView.displayImage(resultImage);
    }

    /** Change processed image brightness with specified offset */
    public void handleBrightness(int offset) {
        imageModel.setDisplayedImageOffset(offset);
        handleColor();
    }

    /** Change processed image contrast with specified scale */
    public void handleContrast(float scale) {
        imageModel.setDisplayedImageScale(scale);
        handleColor();
    }

    private void handleColor() {
        BufferedImage input = imageModel.getProcessedImage();
        BufferedImage resultImage = ImageProcessing.colorAdjustment(
                input,
                imageModel.getDisplayedImageOffset(),
                imageModel.getDisplayedImageScale()
        );
        imageModel.setDisplayedImage(resultImage);
        mainView.displayImage(resultImage);
    }


    /** Save current displayed image as processed image */
    public void applyChanges() {
        imageModel.setDisplayedImageScale(1f);
        imageModel.setDisplayedImageOffset(0);
        addToUndoStack(imageModel.getProcessedImage());
        imageModel.setProcessedImage(imageModel.getDisplayedImage());
    }

    public void closedDialogWindow() {
        imageModel.setDisplayedImageScale(1f);
        imageModel.setDisplayedImageOffset(0);
        mainView.displayImage(imageModel.getProcessedImage());
    }

    public void exit() {
        System.exit(0);
    }


    private void addToUndoStack(BufferedImage prevImage) {
        undoStack.push(prevImage);

        mainView.enableRedo(false);
        redoStack.clear();
    }


}
