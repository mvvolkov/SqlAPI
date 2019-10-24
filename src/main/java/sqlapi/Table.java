package sqlapi;

import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionResult.SelectionResultSet;

import java.util.List;

public interface Table {

    TableMetadata getMetadata();

    void insert(List<Object> values) throws WrongValueTypeException, ConstraintException;

    void insert(List<String> columns, List<Object> values)
            throws WrongValueTypeException, ConstraintException;

    void insert(SelectionResultSet rows);

    void delete(SelectionPredicate selectionPredicate);

    void update(List<AssignmentOperation> assignmentOperations, SelectionPredicate selectionPredicate);

    SelectionResultSet select(List<SelectionUnit> selectionUnits, SelectionPredicate selectionPredicate) throws Exception;
}
