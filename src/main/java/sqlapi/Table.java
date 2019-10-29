package sqlapi;

import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionResult.ResultSet;

import java.util.List;

public interface Table {

    TableMetadata getMetadata();

    Database getDatabase();

    void checkPrimaryKey(ColumnReference columnReference, Object value)
            throws ConstraintException, WrongValueTypeException;

    void insert(List<Object> values) throws WrongValueTypeException, ConstraintException;

    void insert(List<String> columns, List<Object> values)
            throws WrongValueTypeException, ConstraintException;

    void insert(ResultSet rows);

    void delete(SelectionPredicate selectionPredicate);

    void update(List<AssignmentOperation> assignmentOperations, SelectionPredicate selectionPredicate);

    ResultSet select(List<SelectedColumn> selectedColumns, SelectionPredicate selectionPredicate) throws WrongValueTypeException;
}
