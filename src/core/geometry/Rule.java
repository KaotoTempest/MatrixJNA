package core.geometry;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface Rule {
    double get();

    Set<Variable> getVariables();

    default double partial(@NotNull Variable v) {
        if(!getVariables().contains(v))
            return 0;

        double originalValue = v.get();
        double a = get();
        double dV = /*originalValue * */0.01;
        v.set(originalValue + dV); // add 1% diff //
        double b = get();
        v.set(originalValue);
        return (b - a) / dV;
    }
}
