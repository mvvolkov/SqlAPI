package clientImpl.metadata;

import api.metadata.ColumnMetadata;
import org.jetbrains.annotations.NotNull;

public class ColumnMetadataImpl<T extends Comparable<T>>
        implements ColumnMetadata<T> {

    @NotNull
    private final String columnName;

    @NotNull
    public final String typeName;

    @NotNull
    private final Class<T> javaClass;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;

    private final int size;


    protected ColumnMetadataImpl(@NotNull Builder<T> builder) {
        this.columnName = builder.columnName;
        this.typeName = builder.typeName;
        this.isNotNull = builder.isNotNull;
        this.isPrimaryKey = builder.isPrimaryKey;
        this.javaClass = builder.javaClass;
        this.size = builder.size;
    }

    @NotNull
    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override public String getSqlTypeName() {
        return null;
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
    public Class<T> getJavaClass() {
        return javaClass;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static class Builder<T extends Comparable<T>> {

        @NotNull
        private final String columnName;

        @NotNull
        private final String typeName;

        @NotNull
        private Class<T> javaClass;

        private boolean isNotNull = false;

        private boolean isPrimaryKey = false;

        private int size = -1;


        public Builder(@NotNull String columnName, @NotNull String typeName,
                       @NotNull Class javaClass) {
            this.columnName = columnName;
            this.typeName = typeName;
            this.javaClass = javaClass;
        }

        public Builder(@NotNull String columnName, @NotNull String typeName,
                       @NotNull Class javaClass, int size) {
            this(columnName, typeName, javaClass);
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


        public ColumnMetadataImpl<T> build() {
            return new ColumnMetadataImpl<T>(this);
        }

        public void setSize(int size) {
            this.size = size;
        }
    }


}
