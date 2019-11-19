package clientImpl.queries;

import sqlapi.metadata.ColumnMetadata;
import sqlapi.queries.CreateTableStatement;

import java.util.List;

public class CreateTableStatementImpl extends AbstractSqlStatement
        implements CreateTableStatement {


    private final List<ColumnMetadata> columns;

    public CreateTableStatementImpl(String databaseName, String tableName,
                                    List<ColumnMetadata> columns) {
        super(databaseName, tableName);
        this.columns = columns;
    }

    @Override
    public List<ColumnMetadata> getColumns() {
        return columns;
    }
}
