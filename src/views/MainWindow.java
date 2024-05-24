package views;

import controller.AppController;
import views.modals.ColorAdjustDialog;
import views.modals.FiltrationDialog;
import views.viewarea.MouseEventsListener;
import views.viewarea.ViewArea;

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
     * Controller used for handling user interactions.
     */
    private final AppController appController;

    private final MainWindowMenubar menubar;
    private final MainWindowToolbar toolbar;

    private final ViewArea viewArea;

    private final FiltrationDialog filtrationDialog;
    private final ColorAdjustDialog colorAdjustDialog;


    public MainWindow(AppController appController) {
        this.appController = appController;

        this.menubar = new MainWindowMenubar();
        this.toolbar = new MainWindowToolbar();
        this.viewArea = new ViewArea();

        MouseEventsListener mouseEventsListener = new MouseEventsListener(this.viewArea);
        this.viewArea.addMouseListener(mouseEventsListener);
        this.viewArea.addMouseMotionListener(mouseEventsListener);
        this.viewArea.addMouseWheelListener(mouseEventsListener);

        this.filtrationDialog = new FiltrationDialog(this, true);
        this.colorAdjustDialog = new ColorAdjustDialog(this, true);

        actionsSetup();
        filtrationDialogSetup();
        colorAdjustmentDialogSetup();

        mainWindowSetup();
    }


    /**
     * Show dialog for user to choose one image file from user's computer.
     * @return absolute path (String) to the selected file or null if user close dialog.
     */
    @Override
    public String getImageSourceFromUser() {
        JFileChooser fileChooser = new JFileChooser(".");
        FileFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
        fileChooser.setFileFilter(filter);

        int response = fileChooser.showOpenDialog(this);
        if(response == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * Show dialog for user to set  user's path for saving edited image.
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
     * Display provided image in the view area.
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
     * Enables/Disables these toolbar and menubar actions that require loaded image.
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
     */
    @Override
    public void enableUndo(boolean enable) {
        if(menubar.undo.isEnabled() != enable) {
            menubar.undo.setEnabled(enable);
        }
    }

    /**
     * Set redo button mode.
     */
    @Override
    public void enableRedo(boolean enable) {
        menubar.redo.setEnabled(enable);
    }




    /**
     * Connect filtration dialog actions with controller.
     */
    private void filtrationDialogSetup() {
        filtrationDialog.runButton.addActionListener(e -> {
            appController.handleFiltration(filtrationDialog.getKernel());
            filtrationDialog.dispose();
        });
    }

    /**
     * Connect color adjustment dialog with controller.
     */
    private void colorAdjustmentDialogSetup() {
        colorAdjustDialog.brightnessSlider.addChangeListener(e -> appController.handleBrightness(colorAdjustDialog.brightnessSlider.getValue()));
        colorAdjustDialog.contrastSlider.addChangeListener(e -> appController.handleContrast(colorAdjustDialog.getContrastValue()));
        colorAdjustDialog.submitButton.addActionListener(e -> {
            appController.applyChanges();
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
        menubar.open.addActionListener(e -> appController.handleLoadImage());
        menubar.save.addActionListener(e -> appController.handleSaveImage());
        menubar.exit.addActionListener(e -> appController.exit());

        menubar.undo.addActionListener(e -> appController.handleUndo());
        menubar.redo.addActionListener(e -> appController.handleRedo());

        toolbar.grayScaleButton.addActionListener(e -> appController.handleToGrayScale());
        toolbar.filtrationButton.addActionListener(e -> filtrationDialog.setVisible(true));
        toolbar.adjustColorButton.addActionListener(e -> colorAdjustDialog.setVisible(true));
    }


    /**
     * Setup main window design.
     */
    private void mainWindowSetup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));
        setLocation(200, 100);

        setTitle("PhotoEditor");
        setIconImage(new ImageIcon("resources/camera.png").getImage());

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
}
