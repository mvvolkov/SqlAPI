package sqlapi;

import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionResult.SelectionResultRow;
import sqlapi.selectionResult.SelectionResultSet;

import java.util.List;

public interface Table {

    TableMetadata getMetadata();

    void insert(List<Object> values) throws WrongValueTypeException, ConstraintException;

    void insert(List<String> columns, List<Object> values) throws WrongValueTypeException, ConstraintException;

    void insert(SelectionResultSet rows);

    void delete(AbstractPredicate selectionPredicate);

    void update(List<AssignmentOperation> assignmentOperations, AbstractPredicate selectionPredicate);

    List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, AbstractPredicate selectionPredicate);
}
