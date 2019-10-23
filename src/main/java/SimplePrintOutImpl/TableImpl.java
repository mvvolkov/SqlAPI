package SimplePrintOutImpl;

import sqlapi.*;
import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.selectionResult.SelectionResultRow;
import sqlapi.selectionResult.SelectionResultSet;

import java.util.Arrays;
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
    public void insert(SelectionResultSet rows) {

    }


    @Override
    public void delete(AbstractPredicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(metadata.getName());
        if (selectionPredicate.isNotEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectionPredicate.toString());
        }
        System.out.println(sb);
    }

    @Override
    public void update(List<AssignmentOperation> assignmentOperations, AbstractPredicate selectionPredicate) {
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
    public List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, AbstractPredicate selectionPredicate) {
        StringBuilder sb = new StringBuilder("SELECT ");
        String from = selectionUnits.stream().map(SelectionUnit::toString).collect(Collectors.joining(", "));
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
