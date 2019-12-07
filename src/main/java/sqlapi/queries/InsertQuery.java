package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.InputValue;

import java.util.List;

public interface InsertQuery extends TableQuery {

    @NotNull List<String> getColumns();

    @NotNull List<InputValue> getInputValues();
}
