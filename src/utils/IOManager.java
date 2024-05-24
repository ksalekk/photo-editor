package utils;

import com.sun.jdi.InvalidTypeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for service input/output images operations.
 */
public class IOManager {

    /**
     * Load image from specified path and returns it as the BufferedImage.
     */
    public static BufferedImage loadImage(String absPath) throws IOException, InvalidTypeException {
        BufferedImage img = ImageIO.read(new File(absPath));
        if(img.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
            throw new InvalidTypeException("Indexed image is not supported");
        }
        return img;
    }

    /**
     * Save image in specified path.
     * @param img saved image
     * @param absPath absolute path for saving location
     * @param formatName image format
     */
    public static void saveImage(BufferedImage img, String absPath, String formatName) {
        try {
            ImageIO.write(img, formatName, new File(absPath + "." + formatName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
