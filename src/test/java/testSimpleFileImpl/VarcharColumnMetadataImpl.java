package testSimpleFileImpl;

import org.jetbrains.annotations.NotNull;
import sqlapi.*;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;

public final class VarcharColumnMetadataImpl extends ColumnMetadataImpl<String> {

    /**
     * Maximal number of characters.
     */
    private final int maxLength;

    public static final String TYPE_NAME = "VARCHAR";

    private VarcharColumnMetadataImpl(@NotNull Builder builder) {
        super(builder);
        this.maxLength = builder.maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @NotNull
    @Override
    public String getSqlTypeName() {
        return TYPE_NAME;
    }

    @NotNull
    @Override
    protected String getTypeSpecificDescription() {
        return "(" + String.valueOf(maxLength) + ")";
    }

    public static Builder builder(String name, int maxLength) {
        return new Builder(name, maxLength);
    }

    public static class Builder extends ColumnMetadataImpl.Builder<Builder, String> {

        private final int maxLength;

        public Builder(@NotNull String name, int maxLength) {
            super(name, String.class);
            this.maxLength = maxLength;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ColumnMetadataImpl build() {
            return new VarcharColumnMetadataImpl(this);
        }
    }

    @Override
    public void checkConstraints(Table table, Object value) throws WrongValueTypeException, ConstraintException {
        super.checkConstraints(table, value);

        TableMetadata tableMetadata = table.getMetadata();
        Database database = table.getDatabase();
        String tableName = tableMetadata.getName();
        String dbName = database.getName();
        ColumnReference columnReference = new ColumnReference(this.getName(), tableName, dbName);
        String stringValue = (String) value;
        if (stringValue.length() > maxLength) {
            throw new ConstraintException(columnReference, "Maximal length exceeded");
        }
    }
}
