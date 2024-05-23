package views.viewarea;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents the area, which display image (allows to move and zoom image). It includes 2 coordinates systems:
 *  - original - (0, 0) anchored with upper left of JPanel (x-axis go right, y-axis goes down)
 *  - transformed - (0, 0) anchored with upper left of image (x-axis go right, y-axis goes down)
 */
public class ViewArea extends JPanel {

    protected BufferedImage displayedImage = null;
    protected final Point translation = new Point(0, 0);
    protected float scaling = 1;

    /**
     * Offset to center img
     */
    private final Point OFFSET_POINT = new Point(0, 0);


    public ViewArea() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.decode("#111111"));
    }


    /**
     * Set current displayed image and parameters.
     * @param displayedImage BufferedImage that will be displayed.
     */
    public void setDisplayedImage(BufferedImage displayedImage) {

        if(displayedImage != null) {
            this.displayedImage = displayedImage;
            setPreferredSize(new Dimension(displayedImage.getWidth(), displayedImage.getHeight()));
            repaint();
        } else {
            this.displayedImage = null;
            repaint();
        }
//        imageUpperLeft.setLocation(this.imageUpperLeft.getX(), imageUpperLeft.getY());
//        currentZoom = 1;
    }

    /**
     * Center image on the view area.
     */
    public void centerImage() {
        if(displayedImage != null) {
//            OFFSET_POINT.setLocation(
//                    (getWidth() - displayedImage.getWidth(null)) / 2,
//                    (getHeight() - displayedImage.getHeight(null)) / 2
//            );
//        } else {
//            OFFSET_POINT.translate(getWidth()/2, getHeight()/2);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        if(displayedImage != null) {
            g2d.scale(scaling, scaling);
            g2d.translate(translation.x, translation.y);
            g2d.drawImage(displayedImage, null, 0, 0);
        }

        g2d.dispose();
    }
}
