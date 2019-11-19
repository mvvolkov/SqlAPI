package sqlapi.queries;

import java.util.List;

public interface InsertStatement extends SqlStatement {

    List<String> getColumns();

    List<Object> getValues();

    @Override
    default Type getType() {
        return Type.INSERT;
    }
}
