package sqlapi.queryResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface QueryResultRow {


    @Nullable Object getValue(int index);

    @NotNull List<Object> getValues();
}
