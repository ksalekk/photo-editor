package imageprocessing;

import java.awt.*;
import java.awt.image.*;

/**
 * Processor class that is responsible for image processing. It may be used as static class.
 */
public class ImageProcessing {
    /**
     * Convert input image to gray scale.
     * @param input converted image.
     * @return input image converted to gray scale
     */
    public static BufferedImage RGBtoGray(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int y = 0; y < input.getHeight(); y++) {
            for(int x = 0; x < input.getWidth(); x++) {
                int argb = input.getRGB(x, y);
                output.setRGB(x, y, argb);
            }
        }
        return output;
    }

    /**
     * Convolve image with 3x3 kernel mask passed as numbers array
     * @param input input image
     * @param kernelElements kernel mask
     * @return result image
     */
    public static BufferedImage linearFiltration(BufferedImage input, float[] kernelElements) {
        ConvolveOp filtration = new ConvolveOp(new Kernel(3, 3, kernelElements));
        return filtration.filter(input, null);
    }

    /**
     * Change image contrast or/and brightness.
     * @param input input image
     * @param offset offset added to every pixel of input image
     * @param scale scaling factor for multiplying every pixel value (gray scale) or multiplying
     *             brightness channel value in HSB color space for every pixel
     * @return result image
     */
    public static BufferedImage colorAdjustment(BufferedImage input, int offset, float scale) {
        BufferedImage result = input;
        if(offset != 0) {
            result = changeBrightness(result, offset);
        }
        if (scale != 1) {
            result = changeContrast(result, scale);
        }

        return result;
    }

    /**
     * Change image brightness by add offset to every pixel of input image.
     * @param input input image
     * @param offset offset added to every pixel of input image
     * @return result image
     */
    private static BufferedImage changeBrightness(BufferedImage input, int offset) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        BufferedImageOp brightness = new RescaleOp(
                1,
                offset,
                new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        );
        brightness.filter(input, output);
        return output;
    }

    /**
     * Change image contrast by multiplying pixel value by scale factor (gray scale) or
     * multiplying brightness channel value in HSB color space by scale factor for every pixel
     * @param input input image
     * @param scale scale factor for multiplying
     * @return result image
     */
    private static BufferedImage changeContrast(BufferedImage input, float scale) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

        if(input.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            BufferedImageOp contrast = new RescaleOp(
                    scale,
                    0,
                    new RenderingHints(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    )
            );
            contrast.filter(input, output);

        } else {
            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    int argb = input.getRGB(x, y);
                    int r = (argb >> 16) & 0xff;
                    int g = (argb >> 8) & 0xff;
                    int b = argb & 0xff;


                    float[] hsbValues = new float[3];
                    Color.RGBtoHSB(r, g, b, hsbValues);
                    float hue = hsbValues[0];
                    float saturation = hsbValues[1];
                    float brightness = hsbValues[2];

                    brightness = (float) (scale * (brightness - 0.5) + 0.5);
                    brightness = Math.min(1, Math.max(0, brightness));

                    int rgb = Color.HSBtoRGB(hue, saturation, brightness);

                    output.setRGB(x, y, rgb);
                }
            }
        }

        return output;
    }
}
