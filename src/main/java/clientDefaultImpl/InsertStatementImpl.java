package clientDefaultImpl;

import api.InsertStatement;

import java.util.List;

public class InsertStatementImpl extends AbstractSqlStatement implements InsertStatement {


    private final List<String> columns;
    private final List<Object> values;

    public InsertStatementImpl(String databaseName, String tableName, List<String> columns, List<Object> values) {
        super(databaseName, tableName);
        this.columns = columns;
        this.values = values;
    }

    public InsertStatementImpl(String databaseName, String tableName, List<Object> values) {
        this(databaseName, tableName, null, values);
    }


    @Override
    public List<Object> getValues() {
        return values;
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }
}
