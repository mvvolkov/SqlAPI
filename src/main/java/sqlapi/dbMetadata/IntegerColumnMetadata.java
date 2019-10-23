package sqlapi.dbMetadata;

import org.jetbrains.annotations.NotNull;

public final class IntegerColumnMetadata extends ColumnMetadata<Integer> {

    public static final String TYPE_NAME = "INTEGER";

    private IntegerColumnMetadata(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    public @NotNull String getTypeName() {
        return TYPE_NAME;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }


    public static class Builder extends ColumnMetadata.Builder<Builder, Integer> {

        public Builder(@NotNull String name) {
            super(name, Integer.class);
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
