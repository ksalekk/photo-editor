package views;

import controller.AppController;
import imageprocessing.ColorAdjustDialog;
import imageprocessing.FiltrationDialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * The MainWindow class represents the main GUI window of the application.
 */
public class MainWindow extends JFrame implements MainView {

    /**
     * Controller used for handling user requests.
     */
    private final AppController appController;

    /**
     * Main window menu bar. Allows user to execute some app/file actions (loading, saving, exit).
     */
    private final MainWindowMenubar menubar;

    /**
     * Main window toolbar. Allows user to execute image processing actions on loaded image.
     */
    private final MainWindowToolbar toolbar;

    /**
     * Scrollable and draggable box that stores image and allows user to interact.
     */
    private final ViewArea viewArea;

    /**
     * Convolution filter dialog.
     */
    private final FiltrationDialog filtrationDialog;

    /**
     * Brightness and contrast adjustment dialog
     */
    private final ColorAdjustDialog colorAdjustDialog;


    public MainWindow(AppController appController) {
        this.appController = appController;

        this.menubar = new MainWindowMenubar();
        this.toolbar = new MainWindowToolbar();
        this.viewArea = new ViewArea();

        this.filtrationDialog = new FiltrationDialog(this, true);
        this.colorAdjustDialog = new ColorAdjustDialog(this, true);

        actionsSetup();
        filtrationDialogSetup();
        colorAdjustmentDialogSetup();

        mainWindowSetup();
    }

    /**
     * Show dialog for user to choose one image file from user's computer. Return a string that is
     * the absolute path to the selected file or null if user close dialog.
     * @return image file absolute path if user choose approve option and null otherwise.
     */
    @Override
    public String getImageSourceFromUser() {
        JFileChooser fileChooser = new JFileChooser(".");
        FileFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int response = fileChooser.showOpenDialog(this);
        if(response == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * Show dialog for user to set  user's path to saving edited image.
     * @return String object that is the absolute path to the selected directory and filename
     * or null if user close dialog.
     */
    @Override
    public String getImageDestinationFromUser() {
        JFileChooser fileChooser = new JFileChooser(".");
        int response = fileChooser.showSaveDialog(this);
        if(response == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * Display provided image in view area.
     * @param image represents edited image that will be displayed
     */
    @Override
    public void displayImage(BufferedImage image) {
        viewArea.setDisplayedImage(image);
    }

    /**
     * Draws an empty panel in ViewArea object.
     */
    @Override
    public void hideImage() {
        viewArea.setDisplayedImage(null);
    }

    /**
     * Enables/Disables image processing and file saving, closing buttons
     * @param imageIsLoaded True to enable, false to disable.
     */
    @Override
    public void setViewMode(boolean imageIsLoaded) {
        toolbar.grayScaleButton.setEnabled(imageIsLoaded);
        toolbar.filtrationButton.setEnabled(imageIsLoaded);
        toolbar.adjustColorButton.setEnabled(imageIsLoaded);

        menubar.save.setEnabled(imageIsLoaded);
    }

    /**
     * Set undo button mode.
     * @param enable True to enable, false to disable the "Undo" functionality.
     */
    @Override
    public void enableUndo(boolean enable) {
        if(menubar.undo.isEnabled() != enable) {
            menubar.undo.setEnabled(enable);
        }
    }

    /**
     * Set redo button mode.
     * @param enable True to enable, false to disable the "Redo" functionality.
     */
    @Override
    public void enableRedo(boolean enable) {
        menubar.redo.setEnabled(enable);
    }

    /**
     * Connect filtration dialog actions with controller.
     */
    private void filtrationDialogSetup() {
        filtrationDialog.runButton.addActionListener(e -> appController.handleFiltration(filtrationDialog.getKernel()));
        filtrationDialog.runButton.addActionListener(e -> {
            filtrationDialog.dispose();
        });
    }

    /**
     * Connect color adjustment dialog with controller. Set proper scaling of sliders.
     */
    private void colorAdjustmentDialogSetup() {
        colorAdjustDialog.brightnessSlider.addChangeListener(e -> {
            int offset = colorAdjustDialog.brightnessSlider.getValue();
            appController.handleBrightness(offset);
        });

        colorAdjustDialog.contrastSlider.addChangeListener(e -> {
            float scale = colorAdjustDialog.contrastSlider.getValue();
            appController.handleContrast(scale*0.01f);
        });

        colorAdjustDialog.submitButton.addActionListener(e -> {
            appController.applyChanges();
            colorAdjustDialog.resetSliders();
            colorAdjustDialog.dispose();
        });

        colorAdjustDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                colorAdjustDialog.resetSliders();
                appController.closedDialogWindow();
            }
        });
    }


    /**
     * Connect menu bar and toolbar actions with controller handlers.
     */
    private void actionsSetup() {
        menubar.open.addActionListener(e -> appController.loadImage());
        menubar.save.addActionListener(e -> appController.saveImage());
        menubar.exit.addActionListener(e -> appController.exit());

        menubar.undo.addActionListener(e -> appController.handleUndo());
        menubar.redo.addActionListener(e -> appController.handleRedo());

        toolbar.grayScaleButton.addActionListener(e -> appController.toGrayScale());
        toolbar.filtrationButton.addActionListener(e -> filtrationDialog.setVisible(true));
        toolbar.adjustColorButton.addActionListener(e -> colorAdjustDialog.setVisible(true));
    }


    /**
     * Setup design, connecting with components and disable image processing actions buttons.
     */
    private void mainWindowSetup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));
        setLocation(200, 100);

        setTitle("PhotoEditor");
        setIconImage(new ImageIcon("resources\\camera.png").getImage());

        setJMenuBar(menubar);
        Container windowPane = getContentPane();
        windowPane.add(new JScrollPane(viewArea), BorderLayout.CENTER);
        windowPane.add(toolbar, BorderLayout.NORTH);

        setViewMode(false);
        enableUndo(false);
        enableRedo(false);

        pack();
        setVisible(true);
    }

    @Override
    public void pack() {
        super.pack();
        viewArea.centerImage();
    }
}
