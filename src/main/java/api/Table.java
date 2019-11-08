package api;

import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;
import api.selectionPredicate.Predicate;
import clientDefaultImpl.AssignmentOperationImpl;
import clientDefaultImpl.SelectedItemImpl;

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

    void delete(Predicate selectionPredicate);

    void update(List<AssignmentOperationImpl> assignmentOperations, Predicate selectionPredicate);

    ResultSet select(List<SelectedItemImpl> selectedItems, Predicate selectionPredicate) throws WrongValueTypeException;
}
