package core.geometry;

import core.rendering.Attribute;
import core.rendering.DrawnObject;
import interfaces.VariableSupplier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.List;

public class MutableVector extends DrawnObject implements VariableSupplier {
    public static final MutableVector X = new MutableVector(1, 0);
    public static final MutableVector Y = new MutableVector(0, 1);

    private double d;

    private double x;
    private double y;

    private final Variable vA = new Variable(this::getX, this::setX);
    private final Variable vB = new Variable(this::getY, this::setY);

    public MutableVector(double x, double y) {
        this.x = x;
        this.y = y;

        d = Math.sqrt(x * x + y * y);

        setAttribute(new Attribute(Color.RED, new BasicStroke(1.5f)));
    }

    public static double cross(@NotNull MutableVector a, @NotNull MutableVector b) {
        return a.x * b.y - a.y * b.x;
    }

    public static double dot(@NotNull MutableVector a, @NotNull MutableVector b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double angle(@NotNull MutableVector a, @NotNull MutableVector b) {
        MutableVector _a = a.normalise();
        MutableVector _b = b.normalise();

        return Math.acos(MutableVector.dot(_a, _b)) * MutableVector.cross(_a, _b);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        d = Math.sqrt(x * x + y * y);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        d = Math.sqrt(x * x + y * y);
    }

    public double length() {
        return d;
    }

    public @NotNull MutableVector normalise() {
        return new MutableVector(x / d, y / d);
    }

    public @NotNull MutableVector sub(@NotNull MutableVector b) {
        return new MutableVector(x - b.x, y - b.y);
    }

    public @NotNull MutableVector dof() {
        return new MutableVector(-y, x);
    }

    @Override @NotNull public List<Variable> getVariables() {
        return Arrays.asList(vA, vB);
    }

    @Override public void draw(@NotNull Graphics2D g) {
        super.draw(g);

        Ellipse2D circle = new Ellipse2D.Double(x - 3, y - 3, 6, 6);

        g.draw(circle);
    }
}
