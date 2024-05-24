package views.viewarea;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents scrollable and draggable area, that stores image and allows user to interact with it  (pan and zoom).
 * It includes 2 coordinates systems:
 *  - original - (0, 0) anchored with upper left of JPanel (x-axis go right, y-axis goes down)
 *  - transformed - (0, 0) anchored with upper left of image (x-axis go right, y-axis goes down)
 */
public class ViewArea extends JPanel {

    protected BufferedImage displayedImage = null;
    protected final Point translation = new Point(0, 0);
    protected float scaling = 1;

    public ViewArea() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.decode("#111111"));
    }


    /**
     * Set current displayed image and parameters.
     * @param displayedImage BufferedImage that will be displayed.
     */
    public void setDisplayedImage(BufferedImage displayedImage) {
        this.displayedImage = displayedImage;
        if(displayedImage != null) {
            setPreferredSize(new Dimension(displayedImage.getWidth(), displayedImage.getHeight()));
        }
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        if(displayedImage != null) {
            g2d.translate(translation.x, translation.y);
            g2d.scale(scaling, scaling);
            g2d.drawImage(displayedImage, null, 0, 0);
        }

        g2d.dispose();
    }
}
