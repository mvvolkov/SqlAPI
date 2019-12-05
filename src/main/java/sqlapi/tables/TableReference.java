package sqlapi.tables;

import java.util.List;

public interface TableReference {

    default void setParameters(List<Object> parameters) {
    }
}
