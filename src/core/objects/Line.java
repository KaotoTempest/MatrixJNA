package core.objects;

import core.geometry.MutableVector;
import core.geometry.Variable;
import core.rendering.DrawnObject;
import interfaces.JointedObject;
import interfaces.VariableSupplier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Line extends DrawnObject implements JointedObject, VariableSupplier {
    @NotNull private final MutableVector a;
    @NotNull private final MutableVector b;
    @NotNull private final ArrayList<MutableVector> set = new ArrayList<>(2);

    public Line(@NotNull MutableVector a, @NotNull MutableVector b) {
        this.a = a;
        this.b = b;

        set.add(0, a);
        set.add(1, b);
    }

    @NotNull public MutableVector getA() {
        return a;
    }

    @NotNull public MutableVector getB() {
        return b;
    }

    @NotNull public MutableVector getVector() {
        return b.sub(a);
    }

    @Override public void draw(@NotNull Graphics2D g) {
        super.draw(g);

        Line2D.Double line = new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());

        g.draw(line);
    }

    @Override @NotNull public Collection<MutableVector> getJoints() {
        return set;
    }

    @Override @NotNull public java.util.List<Variable> getVariables() {
        List<Variable> variables = new LinkedList<>();
        variables.addAll(a.getVariables());
        variables.addAll(b.getVariables());
        return variables;
    }
}
