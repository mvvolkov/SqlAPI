package clientImpl.queries;

import api.queries.InsertStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class InsertStatementImpl extends AbstractSqlStatement implements InsertStatement {

    @NotNull
    private final List<String> columns;

    @NotNull
    private final List<Object> values;

    public InsertStatementImpl(String databaseName, String tableName,
                               List<String> columns, List<Object> values) {
        super(databaseName, tableName);
        this.columns = columns;
        this.values = values;
    }

    public InsertStatementImpl(String databaseName, String tableName,
                               List<Object> values) {
        this(databaseName, tableName, Collections.emptyList(), values);
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
