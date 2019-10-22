package sqlapi;

import java.util.List;

public interface Table {
    
    TableMetadata getMetadata();

    void insert(List<Object> values) throws Exception;

    void insert(List<String> columns, List<Object> values);

    void insert(SelectionResultSet rows);

    void delete(SelectionCriteria selectionCriteria);

    void update(List<AssignmentOperation> assignmentOperations, SelectionCriteria selectionCriteria);

    List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, SelectionCriteria selectionCriteria);
}
