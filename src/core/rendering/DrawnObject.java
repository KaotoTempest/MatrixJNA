package core.rendering;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class DrawnObject {
    private Attribute attr = new Attribute(Color.black, new BasicStroke(1.5f));

    public void draw(@NotNull Graphics2D g) {
        g.setColor(getAttribute().color);
        g.setStroke(getAttribute().stroke);
    }

    @NotNull Attribute getAttribute() {
        return attr;
    }

    public void setAttribute(@NotNull Attribute attr) {
        this.attr = attr;
    }
}
