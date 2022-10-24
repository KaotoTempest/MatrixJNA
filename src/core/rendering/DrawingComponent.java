package core.rendering;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DrawingComponent extends JPanel {
    private List<DrawnObject> objectList = new LinkedList<>();

    @Override public void update(Graphics g) {
        BufferedImage im = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(im.getGraphics());
        g.drawImage(im, 0, 0, null);
    }

    @Override public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        objectList.forEach(o -> o.draw(g2));
    }

    public boolean add(@NotNull DrawnObject... drawnObjects) {
        return Collections.addAll(objectList, drawnObjects);
    }

    public boolean remove(@NotNull DrawnObject o) {
        return objectList.remove(o);
    }
}
