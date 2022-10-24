package core.geometry.rules;

import core.geometry.Rule;
import core.geometry.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class FixedRule implements Rule {
    private final double V;
    @NotNull private final Variable v;

    public FixedRule(@NotNull Variable v) {
        this.v = v;
        this.V = v.get();
    }

    @Override public double get() {
        return v.get() - V;
    }

    @Override public Set<Variable> getVariables() {
        return Collections.singleton(v);
    }
}
