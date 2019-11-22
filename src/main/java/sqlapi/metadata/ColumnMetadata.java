package sqlapi.metadata;

import java.util.Collection;

public interface ColumnMetadata {

    String getColumnName();

    SqlType getSqlType();

    Collection<ColumnConstraint> getConstraints();
    
    default int getSize() {
        return -1;
    }

//    Object getDefaultValue();

}
