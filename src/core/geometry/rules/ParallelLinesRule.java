package core.geometry.rules;

import core.SetUtils;
import core.geometry.MutableVector;
import core.geometry.Rule;
import core.geometry.Variable;
import core.objects.Line;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ParallelLinesRule implements Rule {
    @NotNull private final Line a;
    @NotNull private final Line b;

    public ParallelLinesRule(@NotNull Line a, @NotNull Line b) {
        this.a = a;
        this.b = b;
    }

    @Override public double get() {
        return MutableVector.cross(a.getVector().normalise(), b.getVector().normalise());
    }

    @Override public Set<Variable> getVariables() {
        return SetUtils.union(a.getVariables(), b.getVariables());
    }
}
