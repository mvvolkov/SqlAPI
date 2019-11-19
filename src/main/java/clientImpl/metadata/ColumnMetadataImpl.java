package clientImpl.metadata;

import api.metadata.ColumnMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnMetadataImpl implements ColumnMetadata {

    @NotNull
    private final String columnName;

    @NotNull
    public final SqlType sqlType;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;

    private final int size;

    @Nullable
    private final Object defaultValue;


    protected ColumnMetadataImpl(@NotNull Builder builder) {
        this.columnName = builder.columnName;
        this.sqlType = builder.sqlType;
        this.isNotNull = builder.isNotNull;
        this.isPrimaryKey = builder.isPrimaryKey;
        this.size = builder.size;
        this.defaultValue = builder.defaultValue;
    }

    @NotNull
    @Override
    public String getColumnName() {
        return columnName;
    }

    @NotNull
    @Override
    public SqlType getSqlType() {
        return sqlType;
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
        sb.append(this.getSqlType());
        if (this.getSize() != -1) {
            sb.append("(");
            sb.append(this.getSize());
            sb.append(")");
        }
        if (this.isNotNull()) {
            sb.append(" NOT NULL");
        }
        if (this.isPrimaryKey()) {
            sb.append(" PRIMARY KEY");
        }
        if (defaultValue != null) {
            sb.append(" DEFAULT ");
            sb.append(defaultValue);
        }
        return sb.toString();
    }


    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    public static class Builder {

        @NotNull
        private final String columnName;

        @NotNull
        private final SqlType sqlType;

        private boolean isNotNull = false;

        private boolean isPrimaryKey = false;

        private int size = -1;

        @NotNull
        private Object defaultValue;


        public Builder(@NotNull String columnName, @NotNull SqlType sqlType) {
            this.columnName = columnName;
            this.sqlType = sqlType;
        }

        public Builder(@NotNull String columnName, @NotNull SqlType sqlType, int size) {
            this(columnName, sqlType);
            this.size = size;
        }

        public Builder notNull() {
            isNotNull = true;
            return this;
        }

        public Builder primaryKey() {
            isPrimaryKey = true;
            return this;
        }

        public Builder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }


        public ColumnMetadataImpl build() {
            return new ColumnMetadataImpl(this);
        }
    }
}
