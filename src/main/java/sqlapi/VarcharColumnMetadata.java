package sqlapi;

import org.jetbrains.annotations.NotNull;

public final class VarcharColumnMetadata extends ColumnMetadata<String> {

    /**
     * Maximal number of characters.
     */
    private final int maxLength;

    public static final String TYPE_NAME = "VARCHAR";

    private VarcharColumnMetadata(@NotNull Builder builder) {
        super(builder);
        this.maxLength = builder.maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @NotNull
    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @NotNull
    @Override
    protected String getTypeSpecificDescription() {
        return "(" + String.valueOf(maxLength) + ")";
    }

    public static class Builder extends ColumnMetadata.Builder<Builder, String> {

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
        public ColumnMetadata build() {
            return new VarcharColumnMetadata(this);
        }
    }
}
