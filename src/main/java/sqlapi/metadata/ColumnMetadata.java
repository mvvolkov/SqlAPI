package sqlapi.metadata;

import java.util.Collection;

public interface ColumnMetadata {

    String getColumnName();

    SqlType getSqlType();

    Collection<ColumnConstraint> getConstraints();
}
