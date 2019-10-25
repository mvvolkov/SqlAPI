package sqlapi;

import org.jetbrains.annotations.Nullable;

public abstract class TableReference {

    @Nullable
    protected final String alias;

    protected TableReference(@Nullable String alias) {
        this.alias = alias;
    }
}
