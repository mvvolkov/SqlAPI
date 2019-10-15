package api;

import org.jetbrains.annotations.NotNull;

public final class IntegerColumnDescription extends ColumnDescription {

    public static final String TYPE_NAME = "VARCHAR";

    private IntegerColumnDescription(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    public @NotNull String getTypeName() {
        return TYPE_NAME;
    }


    public static class Builder extends ColumnDescription.Builder<Builder> {

        public Builder(@NotNull String name) {
            super(name);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ColumnDescription build() {
            return new IntegerColumnDescription(this);
        }
    }
}
