package clientImpl.queries;

import api.queries.InsertFromSelectStatement;
import api.queries.InsertStatement;
import api.queries.SelectExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class InsertFromSelectStatementImpl extends AbstractSqlStatement implements
        InsertFromSelectStatement {

    @NotNull
    private final List<String> columns;

    private final SelectExpression selectExpression;

    public InsertFromSelectStatementImpl(String tableName,
                                         List<String> columns,
                                         SelectExpression selectExpression) {
        super(tableName);
        this.columns = columns;
        this.selectExpression = selectExpression;
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }

    @Override public SelectExpression getSelectExpression() {
        return selectExpression;
    }
}
