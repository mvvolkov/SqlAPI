package api.queries;

import java.util.List;

public interface InsertStatement extends SqlStatement {

    List<Object> getValues();

    List<String> getColumns();

    @Override
    default Type getType() {
        return Type.INSERT;
    }
}
