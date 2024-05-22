package helpers;

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
     * Load image from passed path.
     * @param absPath absolute path for file
     * @return BufferedImage object that stores image
     * @throws IOException exception during input/output operation
     * @throws InvalidTypeException exception for indexed type image (it must be rgb or gray scale)
     */
    public static BufferedImage loadImage(String absPath) throws IOException, InvalidTypeException {
        BufferedImage img = ImageIO.read(new File(absPath));
        if(img.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
            throw new InvalidTypeException("Indexed image is not serviced");
        }
        return img;
    }

    /**
     * Save image in passed path.
     * @param img saved image
     * @param absPath absolute path for file saving
     * @param formatName image format (e.g. jpg)
     */
    public static void saveImage(BufferedImage img, String absPath, String formatName) {
        try {
            ImageIO.write(img, formatName, new File(absPath + "." + formatName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
