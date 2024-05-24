package model;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * App model, that stores useful data about image.
 */
public class ImageModel {
    /** Loaded image */
    private BufferedImage originalImage = null;
    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
        this.setProcessedImage(originalImage);
    }

    public BufferedImage getOriginalImage() {
        return  this.originalImage;
    }


    /**
     * Current processed image, whose state is saved (not have to be current displayed image).
     */
    private BufferedImage processedImage = null;

    public void setProcessedImage(BufferedImage processedImage) {
        this.processedImage = processedImage;
        setDisplayedImage(processedImage);
    }

    public BufferedImage getProcessedImage() {
        return processedImage;
    }


    /**
     * Current displayed image, which state has been not saved yet. Displayed image became processed image,
     * if user apply changes.
     */
    private BufferedImage displayedImage = null;

    public void setDisplayedImage(BufferedImage displayedImage) {
        this.displayedImage = displayedImage;
    }

    public BufferedImage getDisplayedImage() {
        return displayedImage;
    }



    /**
     * Stores temporary state variables for displayed image
     */
    private int displayedImageOffset = 0;
    private float displayedImageScale = 1f;

    public int getDisplayedImageOffset() {
        return displayedImageOffset;
    }

    public void setDisplayedImageOffset(int displayedImageOffset) {
        this.displayedImageOffset = displayedImageOffset;
    }

    public float getDisplayedImageScale() {
        return displayedImageScale;
    }

    public void setDisplayedImageScale(float displayedImageScale) {
        this.displayedImageScale = displayedImageScale;
    }





}
