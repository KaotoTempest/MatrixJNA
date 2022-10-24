package core.geometry;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A wrapper class that used getters and setters on object to manage variables.
 * ! Its important not to create duplicates !
 */
public class Variable {
    private final Supplier<Double> getter;
    private final Consumer<Double> setter;

    public Variable(Supplier<Double> getter, Consumer<Double> setter) {

        this.getter = getter;
        this.setter = setter;
    }

    public double get() {
        return getter.get();
    }

    public void set(double value) {
        setter.accept(value);
    }
}
