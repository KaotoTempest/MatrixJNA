package core.geometry.rules;

import core.geometry.MutableVector;
import core.geometry.Rule;
import core.geometry.Variable;
import core.objects.Line;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class OrthoRule implements Rule {
    @NotNull private final Line a;

    public OrthoRule(@NotNull Line a) {
        this.a = a;
    }

    @Override public double get() {
        MutableVector u = a.getVector().normalise();
        return MutableVector.cross(u, MutableVector.X) * MutableVector.cross(u, MutableVector.Y);
    }

    @Override public Set<Variable> getVariables() {
        return new HashSet<>(a.getVariables());
    }
}
