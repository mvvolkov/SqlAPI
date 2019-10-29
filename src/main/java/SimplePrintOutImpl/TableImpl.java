package SimplePrintOutImpl;

import SimpleFileImpl.Row;
import SimpleFileImpl.Value;
import sqlapi.*;
import sqlapi.ColumnMetadata;
import sqlapi.TableMetadata;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.OneColumnPredicate;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionResult.ResultSet;

import java.util.List;
import java.util.stream.Collectors;

public class TableImpl implements Table {


    private final Database database;


    private final TableMetadata metadata;

    public TableImpl(Database database, TableMetadata description) {
        this.database = database;
        this.metadata = description;
    }


    @Override
    public TableMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Database getDatabase() {
        return null;
    }

    @Override
    public void checkPrimaryKey(ColumnReference columnReference, Object value)
            throws ConstraintException, WrongValueTypeException {
    }

    private String getStringFromNewValue(Object value) {
        if (value instanceof String) {
            return '\'' + (String) value + '\'';
        } else if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }


    @Override
    public void insert(List<Object> values) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(metadata.getName());
        sb.append(" VALUES (");
        String valuesString = values.stream().map(this::getStringFromNewValue).collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    @Override
    public void insert(List<String> columns, List<Object> values) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(metadata.getName());
        sb.append(" (");
        sb.append(columns.stream().collect(Collectors.joining(", ")));
        sb.append(") VALUES (");
        String valuesString = values.stream().
                map(this::getStringFromNewValue).collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    @Override
    public void insert(ResultSet rows) {

    }


    @Override
    public void delete(SelectionPredicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(metadata.getName());
        if (selectionPredicate.isNotEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        System.out.println(sb);
    }

    @Override
    public void update(List<AssignmentOperation> assignmentOperations, SelectionPredicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(metadata.getName());
        sb.append(" SET ");
        String assignmetns = assignmentOperations.stream().map(AssignmentOperation::toString)
                .collect(Collectors.joining(", "));
        sb.append(assignmetns);
        if (selectionPredicate.isNotEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        System.out.println(sb);
    }

    @Override
    public ResultSet select(List<SelectedColumn> selectedColumns, SelectionPredicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("SELECT ");
        String from = selectedColumns.stream().map(SelectedColumn::toString).collect(Collectors.joining(", "));
        sb.append(from);
        sb.append(" FROM ");
        sb.append(metadata.getName());
        System.out.println(sb);
        if (selectionPredicate.isNotEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        return null;
    }
}
