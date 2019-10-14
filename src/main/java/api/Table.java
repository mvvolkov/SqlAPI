package api;

import java.util.List;

public interface Table {
    
    TableDescription getDescription();

    void insert(List<SqlInsertableValue<?>> values);

    void insert(List<String> columns, List<SqlInsertableValue<?>> values);

    void insert(SqlSelectionResult rows);

    void delete(SqlSelectionCondition selectionCondition);

    void update(List<SqlSelectionResultValue> newValues, SqlSelectionCondition selectionCondition);

    List<SqlSelectionResultRow> select(List<SelectionUnit> selectionUnits, SqlSelectionCondition selectionCondition);
}
