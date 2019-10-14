package api;

import org.jetbrains.annotations.NotNull;


public abstract class ColumnDescription {

    @NotNull
    private final String columnName;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;


    protected ColumnDescription(@NotNull Builder<?> builder) {
        this.columnName = builder.columnName;
        this.isNotNull = builder.isNotNull;
        this.isPrimaryKey = builder.isPrimaryKey;
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

    public abstract static class Builder<T extends Builder<T>> {

        @NotNull
        private final String columnName;

        private boolean isNotNull = false;

        private boolean isPrimaryKey = false;


        public Builder(@NotNull String columnName) {
            this.columnName = columnName;
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

        abstract ColumnDescription build();
    }
}
