package clientImpl.metadata;

import sqlapi.metadata.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MetadataFactory {

    public static ColumnConstraint notNull() {
        return new ColumnConstraintImpl(ColumnConstraintType.NOT_NULL);
    }

    public static ColumnConstraint primaryKey() {
        return new ColumnConstraintImpl(ColumnConstraintType.PRIMARY_KEY);
    }

    public static ColumnConstraint unique() {
        return new ColumnConstraintImpl(ColumnConstraintType.UNIQUE);
    }

    public static ColumnConstraint defaultVal(Object value) {
        return new ColumnConstraintImpl(ColumnConstraintType.DEFAULT_VALUE, Collections.singletonList(value));
    }

    public static ColumnMetadata integer(String columnName) {
        return new ColumnMetadataImpl(columnName, SqlType.INTEGER, -1, Collections.emptyList());
    }

    public static ColumnMetadata integer(String columnName, Collection<ColumnConstraint> constraints) {
        return new ColumnMetadataImpl(columnName, SqlType.INTEGER, -1, constraints);
    }

    public static ColumnMetadata varchar(String columnName, int size) {
        return new ColumnMetadataImpl(columnName, SqlType.VARCHAR, size, Collections.emptyList());
    }

    public static ColumnMetadata varchar(String columnName, int size, Collection<ColumnConstraint> constraints) {
        return new ColumnMetadataImpl(columnName, SqlType.VARCHAR, size, constraints);
    }


    public static TableMetadata tableMetadata(String tableName,
                                              List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }
}
