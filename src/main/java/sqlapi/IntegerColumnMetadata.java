package sqlapi;

import org.jetbrains.annotations.NotNull;

public final class IntegerColumnMetadata extends ColumnMetadata {

    public static final String TYPE_NAME = "VARCHAR";

    private IntegerColumnMetadata(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    public @NotNull String getTypeName() {
        return TYPE_NAME;
    }


    public static class Builder extends ColumnMetadata.Builder<Builder> {

        public Builder(@NotNull String name) {
            super(name);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ColumnMetadata build() {
            return new IntegerColumnMetadata(this);
        }
    }
}
