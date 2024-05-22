package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Represents the area, which display image (allows to move and zoom image).
 */
public class ViewArea extends JPanel {
    /**
     * Current displayed image
     */
    private BufferedImage displayedImage = null;

    /**
     * Current upper left relative to origin top left point (0, 0). Useful for moving image event.
     */
    private final Point imageUpperLeft = new Point(0, 0);

    /**
     * Offset to center img
     */
    private final Point OFFSET_POINT = new Point(0, 0);

    /**
     * Point where dragging has been started
     */
    private Point startDrag = null;

    /**
     * Unit of single scroll event
     */
    private final double ZOOM_FACTOR = 1.2;
    private final double MAX_ZOOM = 5;
    private final double MIN_ZOOM = 0.3;

    /**
     * Current image zoom
     */
    private double currentZoom = 1;

    public ViewArea() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.decode("#111111"));
        eventsSetup();
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
        imageUpperLeft.setLocation(0, 0);
        currentZoom = 1;
    }

    /**
     * Center image on the view area.
     */
    public void centerImage() {
        if(displayedImage != null) {
            OFFSET_POINT.setLocation(
                    (getWidth() - displayedImage.getWidth(null)) / 2,
                    (getHeight() - displayedImage.getHeight(null)) / 2
            );
        } else {
            OFFSET_POINT.translate(getWidth()/2, getHeight()/2);
        }
    }

    /**
     * Set all events for view area (moving and zooming)
     */
    private void eventsSetup() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(displayedImage == null) {
                    return;
                }
                startDrag = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(displayedImage == null) {
                    return;
                }
                startDrag = null;
            }
        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(displayedImage == null) {
                    return;
                }

                if(startDrag != null) {
                    int dx = e.getX() - startDrag.x;
                    int dy = e.getY() - startDrag.y;
                    imageUpperLeft.translate(dx, dy);
                    startDrag = e.getPoint();
                    repaint();
                }
            }
        });


        addMouseWheelListener(e -> {
            if(displayedImage == null) {
                return;
            }

            int rotation = e.getWheelRotation();

            if(rotation<0) {
                currentZoom *= currentZoom<MAX_ZOOM ? -rotation * ZOOM_FACTOR : 1;
            } else {
                currentZoom /= currentZoom>MIN_ZOOM ? rotation * ZOOM_FACTOR : 1;
            }
            repaint();
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        if(displayedImage != null) {
            g2d.translate(imageUpperLeft.x, imageUpperLeft.y);
            g2d.scale(currentZoom, currentZoom);
            g2d.drawImage(displayedImage, null, OFFSET_POINT.x, OFFSET_POINT.y);
        }
        g2d.dispose();
    }
}
