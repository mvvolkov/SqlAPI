package sqlapi.metadata;

import java.util.List;

public interface ColumnConstraint {

    ColumnConstraintType getConstraintType();

    List<Object> getParameters();
}
