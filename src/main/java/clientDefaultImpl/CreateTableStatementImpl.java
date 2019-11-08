package clientDefaultImpl;

import api.ColumnMetadata;
import api.CreateTableStatement;

import java.util.List;

public class CreateTableStatementImpl extends AbstractSqlStatement implements CreateTableStatement {


    private final List<ColumnMetadata> columns;

    public CreateTableStatementImpl(String databaseName, String tableName, List<ColumnMetadata> columns) {
        super(databaseName, tableName);
        this.columns = columns;
    }

    @Override
    public List<ColumnMetadata> getColumns() {
        return columns;
    }
}
