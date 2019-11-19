package sqlapi.queries;

import java.util.List;

public interface InsertFromSelectStatement extends SqlStatement {

    List<String> getColumns();

    SelectExpression getSelectExpression();

    @Override
    default Type getType() {
        return Type.INSERT_FROM_SELECT;
    }
}
