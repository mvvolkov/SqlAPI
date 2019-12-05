package sqlapi.misc;

import java.util.List;

public interface SelectedItem {

    default void setParameters(List<Object> parameters) {
    }
}
