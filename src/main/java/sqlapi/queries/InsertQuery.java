package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnValue;

import java.util.List;

public interface InsertQuery extends TableActionQuery {

    @NotNull List<String> getColumns();

    @NotNull List<ColumnValue> getValues();
}
