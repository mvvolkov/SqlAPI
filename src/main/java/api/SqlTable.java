package api;

import java.util.List;

public interface SqlTable {

    String getName();

    void insert(List<SelectionResultRow> rows);

    void insert(InsertData insertData);

    void delete(SelectionCondition selectionCondition);

    void update(List<SqlValue> newValues, SelectionCondition selectionCondition);

    List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, SelectionCondition selectionCondition);
}
