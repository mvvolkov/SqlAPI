package clientDefaultImpl;

import api.ColumnMetadata;
import api.CreateTableStatement;

import java.util.List;

public class CreateTableStatementImpl implements CreateTableStatement {

    private final String databaseName;
    private final String tableName;
    private final List<ColumnMetadata> columns;

    public CreateTableStatementImpl(String databaseName, String tableName, List<ColumnMetadata> columns) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public List<ColumnMetadata> getColumns() {
        return columns;
    }
}
