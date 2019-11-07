package serverLoggerImpl;

import api.*;
import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;
import api.selectionPredicate.Predicate;
import api.ResultSet;
import clientDefaultImpl.SelectedItemImpl;

import java.util.List;
import java.util.stream.Collectors;

public class TableImpl implements Table {


    private final Database database;


    private final TableMetadata metadata;

    private String tableName;

    private List<ColumnMetadata> columns;

    public TableImpl(Database database, TableMetadata description) {
        this.database = database;
        this.metadata = description;
    }

    public TableImpl(Database database, String tableName, List<ColumnMetadata> columns) {
        this.database = database;
        this.tableName = tableName;
        this.columns = columns;
        metadata = null;
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
        sb.append(database.getName());
        sb.append(".");
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
    public void delete(Predicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(metadata.getName());
        if (!selectionPredicate.isTrue()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        sb.append(";");
        System.out.println(sb);
    }

    @Override
    public void update(List<AssignmentOperation> assignmentOperations, Predicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(metadata.getName());
        sb.append(" SET ");
        String assignmetns = assignmentOperations.stream().map(AssignmentOperation::toString)
                .collect(Collectors.joining(", "));
        sb.append(assignmetns);
        if (!selectionPredicate.isTrue()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        sb.append(";");
        System.out.println(sb);
    }

    @Override
    public ResultSet select(List<SelectedItemImpl> selectedItems, Predicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("SELECT ");
        String from = selectedItems.stream().map(SelectedItemImpl::toString).collect(Collectors.joining(", "));
        sb.append(from);
        sb.append(" FROM ");
        sb.append(metadata.getName());
        if (!selectionPredicate.isTrue()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        sb.append(";");
        System.out.println(sb);
        return null;
    }
}
