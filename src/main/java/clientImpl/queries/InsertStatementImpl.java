package clientImpl.queries;

import api.columnExpr.ColumnValue;
import api.queries.InsertStatement;

import java.util.List;

public class InsertStatementImpl extends AbstractSqlStatement implements InsertStatement {


    private final List<String> columns;
    private final List<ColumnValue> values;

    public InsertStatementImpl(String databaseName, String tableName,
                               List<String> columns, List<ColumnValue> values) {
        super(databaseName, tableName);
        this.columns = columns;
        this.values = values;
    }

    public InsertStatementImpl(String databaseName, String tableName,
                               List<ColumnValue> values) {
        this(databaseName, tableName, null, values);
    }


    @Override
    public List<ColumnValue> getValues() {
        return values;
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }
}
