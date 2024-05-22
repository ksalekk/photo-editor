package views;

import java.awt.image.BufferedImage;

/**
 * MainView is an interface for object that represents main view of the application (it can be app main window).
 * It defines methods that the main view of the application should implement to interact with the user
 * and display image-related functionalities.
 */
public interface MainView {

    /**
     * Show dialog for user to get image source from user (e.g. directory path, file path, URL, ...)
     * and return this source as the string
     * @return String object that represents source of image data
     */
    String getImageSourceFromUser();

    /**
     * Show dialog for user to get image saving destination and return it as a string.
     * @return String object that represents destination of image.
     */
    String getImageDestinationFromUser();

    /**
     * Displays provided image.
     * @param image represents edited image that will be displayed
     */
    void displayImage(BufferedImage image);

    /**
     * Hide displayed image.
     */
    void hideImage();

    /**
     * Enables or disables all components that refers to actions that require loaded and
     * displayed image (e.g. save image, close image, image processing operations etc.)
     * @param imageIsLoaded True to image loaded mode, false otherwise.
     */
    void setViewMode(boolean imageIsLoaded);

    /**
     * Enables or disables the "Undo" functionality.
     * @param enable True to enable, false to disable the "Undo" functionality.
     */
    void enableUndo(boolean enable);

    /**
     * Enables or disables the "Redo" functionality.
     * @param enable True to enable, false to disable the "Redo" functionality.
     */
    void enableRedo(boolean enable);
}
