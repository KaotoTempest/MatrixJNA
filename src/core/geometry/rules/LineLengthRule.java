package core.geometry.rules;

import core.geometry.Rule;
import core.geometry.Variable;
import core.objects.Line;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class LineLengthRule implements Rule {
    @NotNull private final Line a;
    private final double length;

    public LineLengthRule(@NotNull Line a, double length) {

        this.a = a;
        this.length = length;
    }

    @Override public double get() {
        return a.getVector().length() - length;
    }

    @Override public Set<Variable> getVariables() {
        return new HashSet<>(a.getVariables());
    }
}
