package api;

import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;
import clientDefaultImpl.SelectionPredicateImpl;
import api.selectionResult.ResultSet;

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

    void delete(SelectionPredicateImpl selectionPredicate);

    void update(List<AssignmentOperation> assignmentOperations, SelectionPredicateImpl selectionPredicate);

    ResultSet select(List<SelectedColumn> selectedColumns, SelectionPredicateImpl selectionPredicate) throws WrongValueTypeException;
}
