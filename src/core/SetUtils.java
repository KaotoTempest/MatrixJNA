package core;

import core.geometry.Variable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUtils {
    public static <T> Set<T> union(Collection<T>... a) {
        Set<T> result = new HashSet<>();
        for (Collection<T> variables : a) {
            result.addAll(variables);
        }
        return result;
    }
}
