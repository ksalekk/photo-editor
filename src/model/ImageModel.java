package model;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * App model, that stores useful data about image.
 */
public class ImageModel {
    /**
     * Current processed image, which state is saved (not have to be current displayed image).
     */
    private BufferedImage processedImage = null;

    /**
     * Current displayed image, which state has been not saved yet. Displayed image became processed image,
     * if user apply changes.
     */
    private BufferedImage displayedImage = null;

    /**
     * Stores temporary state variables for displayed image
     */
    public int displayedImageOffset = 0;
    public float displayedImageScale = 1f;

    /**
     * Set passed image as processed image.
     * @param processedImage image which state will be saved.
     */
    public void setProcessedImage(BufferedImage processedImage) {
        this.processedImage = processedImage;
        setDisplayedImage(processedImage);
    }

    /**
     * Get processed image.
     * @return processed image
     */
    public BufferedImage getProcessedImage() {
        return processedImage;
    }

    /**
     * Set passed image as displayed image.
     * @param displayedImage - image which is currently displayed.
     */
    public void setDisplayedImage(BufferedImage displayedImage) {
        this.displayedImage = displayedImage;
    }

    /**
     * Get displayed image
     * @return displayed image
     */
    public BufferedImage getDisplayedImage() {
        return displayedImage;
    }
}
