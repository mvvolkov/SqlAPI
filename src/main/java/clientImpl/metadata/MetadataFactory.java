package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.*;

import java.util.*;

public class MetadataFactory {

    public static @NotNull ColumnConstraint notNull() {
        return new ColumnConstraintImpl(ColumnConstraintType.NOT_NULL);
    }

    public static @NotNull ColumnConstraint primaryKey() {
        return new ColumnConstraintImpl(ColumnConstraintType.PRIMARY_KEY);
    }

    public static @NotNull ColumnConstraint unique() {
        return new ColumnConstraintImpl(ColumnConstraintType.UNIQUE);
    }

    public static @NotNull ColumnConstraint defaultVal(Object value) {
        return new ColumnConstraintImpl(ColumnConstraintType.DEFAULT_VALUE,
                Collections.singletonList(value));
    }

    public static @NotNull ColumnConstraint maxSize(int size) {
        return new ColumnConstraintImpl(ColumnConstraintType.MAX_SIZE,
                Collections.singletonList(size));
    }


    public static @NotNull ColumnMetadata integer(@NotNull String columnName,
                                                  @NotNull Collection<ColumnConstraint> constraints) {
        return new ColumnMetadataImpl(columnName, SqlType.INTEGER, constraints);
    }

    public static @NotNull ColumnMetadata integer(@NotNull String columnName,
                                                  ColumnConstraint... constraints) {
        return integer(columnName, Arrays.asList(constraints));
    }

    public static @NotNull ColumnMetadata varchar(@NotNull String columnName, int size) {
        return new ColumnMetadataImpl(columnName, SqlType.VARCHAR,
                Collections.singleton(maxSize(size)));
    }

    public static @NotNull ColumnMetadata varchar(@NotNull String columnName, int size,
                                                  ColumnConstraint... constraints) {
        return varchar(columnName, size, Arrays.asList(constraints));
    }

    public static @NotNull ColumnMetadata varchar(@NotNull String columnName, int size,
                                                  Collection<ColumnConstraint> constraints) {
        Collection<ColumnConstraint> constraints1 = new ArrayList<>(constraints);
        constraints1.add(maxSize(size));
        return new ColumnMetadataImpl(columnName, SqlType.VARCHAR, constraints1);
    }


    public static TableMetadata table(String tableName,
                                      List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }

    public static TableMetadata table(String tableName,
                                      ColumnMetadata... columnsMetadata) {
        return table(tableName, Arrays.asList(columnsMetadata));
    }
}
