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

    public InsertStatementImpl(String tableName,
                               List<String> columns, List<Object> values) {
        super(tableName);
        this.columns = columns;
        this.values = values;
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
