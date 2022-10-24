package interfaces;

import core.geometry.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface VariableSupplier {
    @NotNull List<Variable> getVariables();
}
