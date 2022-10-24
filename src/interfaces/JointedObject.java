package interfaces;

import core.geometry.MutableVector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface JointedObject {
    @NotNull Collection<MutableVector> getJoints();
}
