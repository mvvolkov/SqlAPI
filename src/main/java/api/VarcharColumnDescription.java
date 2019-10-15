package api;

import org.jetbrains.annotations.NotNull;

public final class VarcharColumnDescription extends ColumnDescription {

    /**
     * Maximal number of characters.
     */
    private final int maxLength;

    public static final String TYPE_NAME = "VARCHAR";

    private VarcharColumnDescription(@NotNull Builder builder) {
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

    public static class Builder extends ColumnDescription.Builder<Builder> {

        private final int maxLength;

        public Builder(@NotNull String name, int maxLength) {
            super(name);
            this.maxLength = maxLength;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ColumnDescription build() {
            return new VarcharColumnDescription(this);
        }
    }
}
