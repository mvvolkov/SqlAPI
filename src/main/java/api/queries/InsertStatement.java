package api.queries;

import api.columnExpr.ColumnValue;

import java.util.List;

public interface InsertStatement extends SqlStatement {

    List<ColumnValue<?>> getValues();

    List<String> getColumns();

    @Override
    default Type getType() {
        return Type.INSERT;
    }
}
