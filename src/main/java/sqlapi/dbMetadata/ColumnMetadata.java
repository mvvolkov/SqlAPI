package sqlapi.dbMetadata;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


public abstract class ColumnMetadata<V extends Comparable<V> & Serializable> implements Serializable {

    @NotNull
    private final String columnName;

    @NotNull
    private final Class<V> type;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;


    protected ColumnMetadata(@NotNull Builder<?, V> builder) {
        this.columnName = builder.columnName;
        this.isNotNull = builder.isNotNull;
        this.isPrimaryKey = builder.isPrimaryKey;
        this.type = builder.javaClass;
    }

    @NotNull
    public String getColumnName() {
        return columnName;
    }

    @NotNull
    public abstract String getTypeName();

    public boolean isNotNull() {
        return isNotNull;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getColumnName());
        sb.append(" ");
        sb.append(this.getTypeName());
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
    public Class<V> getType() {
        return type;
    }

    public abstract static class Builder<T extends Builder<T, V>, V extends Comparable<V> & Serializable> {

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

        abstract ColumnMetadata build();
    }
}
