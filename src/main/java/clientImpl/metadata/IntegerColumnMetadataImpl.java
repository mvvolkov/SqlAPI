package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;

public final class IntegerColumnMetadataImpl extends ColumnMetadataImpl<Integer> {

    public static final String TYPE_NAME = "INTEGER";

    private IntegerColumnMetadataImpl(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    public @NotNull String getSqlTypeName() {
        return TYPE_NAME;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }


    public static class Builder extends ColumnMetadataImpl.Builder<Builder, Integer> {

        public Builder(@NotNull String name) {
            super(name, Integer.class);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ColumnMetadataImpl build() {
            return new IntegerColumnMetadataImpl(this);
        }
    }
}
