package clientDefaultImpl;

import org.jetbrains.annotations.NotNull;
import api.*;
import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;

public abstract class ColumnMetadataImpl<V extends Comparable<V>> implements ColumnMetadata {

    @NotNull
    private final String columnName;

    @NotNull
    private final Class<V> type;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;


    protected ColumnMetadataImpl(@NotNull Builder<?, V> builder) {
        this.columnName = builder.columnName;
        this.isNotNull = builder.isNotNull;
        this.isPrimaryKey = builder.isPrimaryKey;
        this.type = builder.javaClass;
    }

    @NotNull
    @Override
    public String getName() {
        return columnName;
    }


    @Override
    public boolean isNotNull() {
        return isNotNull;
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        sb.append(" ");
        sb.append(this.getSqlTypeName());
        sb.append(this.getTypeSpecificDescription());
        if (this.isNotNull()) {
            sb.append(" NOT NULL");
        }
        if (this.isPrimaryKey()) {
            sb.append(" PRIMARY KEY");
        }
        return sb.toString();
    }

    @NotNull
    protected String getTypeSpecificDescription() {
        return "";
    }

    @NotNull
    @Override
    public Class<V> getJavaClass() {
        return type;
    }

    public abstract static class Builder<T extends Builder<T, V>, V extends Comparable<V>>
    implements ColumnMetadataBuilder{

        @NotNull
        private final String columnName;

        @NotNull
        private Class<V> javaClass;

        private boolean isNotNull = false;

        private boolean isPrimaryKey = false;


        public Builder(@NotNull String columnName, Class<V> javaClass) {
            this.columnName = columnName;
            this.javaClass = javaClass;
        }

        public T notNull() {
            isNotNull = true;
            return this.self();
        }

        public T primaryKey() {
            isPrimaryKey = true;
            return this.self();
        }

        // Subclasses must override this method to return "this"
        protected abstract T self();

        public abstract ColumnMetadataImpl build();
    }

    private ColumnReference createColumnReference(ColumnMetadata columnMetadata, String tableName, String dbName) {
        return new ColumnReference(columnMetadata.getName(), tableName, dbName);
    }

    public void checkConstraints(Table table, Object value)
            throws WrongValueTypeException, ConstraintException {

        TableMetadata tableMetadata = table.getMetadata();
        Database database = table.getDatabase();
        String tableName = tableMetadata.getName();
        String dbName = database.getName();

        ColumnReference columnReference = new ColumnReference(this.getName(), tableName, dbName);

        if (value != null && !this.getJavaClass().isInstance(value)) {
            throw new WrongValueTypeException(this.createColumnReference(this,
                    tableMetadata.getName(), database.getName()),
                    this.getJavaClass(), value.getClass());
        }
        if (value == null && this.isNotNull()) {
            throw new ConstraintException(columnReference, "NOT NULL");
        }
        if (this.isPrimaryKey()) {
            table.checkPrimaryKey(columnReference, value);
        }
    }
}
