package views.viewarea;

import java.awt.*;
import java.awt.event.*;

public class MouseEventsListener extends MouseAdapter {
    private final float ZOOM_FACTOR = 1.1f;
    private final float MIN_ZOOM = 0.3f;
    private final float MAX_ZOOM = 100;


    private final ViewArea viewArea;

    private final Point startDraggingPoint = new Point(0, 0);

    // cursor position in context of JPanel coordinate system
    private final Point absoluteCursorPosition = new Point(0, 0);
    // cursor position in context of transformed coordinate system (i.e. displayedImage)
    private final Point relativeCursorPosition = new Point(0, 0);

    private float currentZoom = 1;


    public MouseEventsListener(ViewArea viewArea) {
        this.viewArea = viewArea;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if(viewArea.displayedImage == null) {
            return;
        }
        this.startDraggingPoint.setLocation(e.getPoint());
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if(viewArea.displayedImage == null) {
            return;
        }

        int dx = e.getX() - startDraggingPoint.x;
        int dy = e.getY() - startDraggingPoint.y;
        this.viewArea.translation.translate(dx, dy);
        this.startDraggingPoint.setLocation(e.getPoint());
        viewArea.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.absoluteCursorPosition.setLocation(e.getPoint());
        this.relativeCursorPosition.setLocation(getRelativeTranslation(e.getPoint()));
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(viewArea.displayedImage == null) {
            return;
        }

        int rotationClicksCnt = e.getWheelRotation();
        if(rotationClicksCnt<0) {
            currentZoom *= currentZoom<MAX_ZOOM ? -rotationClicksCnt * ZOOM_FACTOR : 1;
        } else {
            currentZoom /= currentZoom>MIN_ZOOM ? rotationClicksCnt * ZOOM_FACTOR : 1;
        }

        this.viewArea.scaling = currentZoom;
        this.viewArea.repaint();
    }


    private Point getRelativeTranslation(Point point) {
        Point relative = new Point();

        float factor = 1/currentZoom;
        float dx = point.x - this.viewArea.translation.x;
        float dy = point.y - this.viewArea.translation.y;
        relative.setLocation(dx * factor, dy * factor);
        return relative;
    }
}
