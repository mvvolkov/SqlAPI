package localFileDatabase.server.intermediate;

import localFileDatabase.server.LocalFileDbServer;
import localFileDatabase.server.output.QueryResultImpl;
import localFileDatabase.server.output.QueryResultRowImpl;
import localFileDatabase.server.persistent.PersistentTable;
import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.AggregateFunction;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.exceptions.AmbiguousColumnNameException;
import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.UnsupportedTableReferenceTypeException;
import sqlapi.misc.SelectedItem;
import sqlapi.predicates.Predicate;
import sqlapi.queries.SelectQuery;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.tables.*;

import java.util.*;
import java.util.stream.Collectors;

public final class Result {

    private final LocalFileDbServer server;

    private List<ResultHeader> columns;

    private Collection<ResultRow> rows;

    public Result(LocalFileDbServer server) {
        this(server, Collections.emptyList(), Collections.emptyList());
    }

    private Result(LocalFileDbServer server, List<ResultHeader> columns, Collection<ResultRow> rows) {
        this.server = server;
        this.columns = Collections.unmodifiableList(columns);
        this.rows = Collections.unmodifiableCollection(rows);
    }

    public List<ResultHeader> getColumns() {
        return columns;
    }

    public Collection<ResultRow> getRows() {
        return rows;
    }

    public QueryResultImpl getQueryResult(SelectQuery selectQuery) throws SqlException {
        return this.getInternalQueryResult(selectQuery).createOutputResult();
    }

    /**
     * Conceptual Order of Evaluation of a Select Statement
     * 1.First the product of all tables in the from clause is formed.
     * 2.The where clause is then evaluated to eliminate rows that do not satisfy the search_condition.
     * 3.Next, the rows are grouped using the columns in the group by clause.
     * 4.Then, Groups that do not satisfy the search_condition in the having clause are eliminated.
     * 5.Next, the expressions in the select clause target list are evaluated.
     * 6.If the distinct keyword in present in the select clause, duplicate rows are now eliminated.
     * 7.The union is taken after each sub-select is evaluated.
     * 8.Finally, the resulting rows are sorted according to the columns specified in the order by clause.
     */
    private Result getInternalQueryResult(SelectQuery se) throws
            SqlException {

        // 1.
        Result result = this.calcProductOfTables(se.getTableReferences());

        // 2.
        result = result.applyPredicate(se.getPredicate());

        List<SelectedItem> selectedItems = se.getSelectedItems();
//
//        if (selectedItems.isEmpty()) {
//            return result;
//        }
//
//        if (se.getGroupByColumns().isEmpty()) {
//            return result.getSelectedValues(selectedItems);
//        }

        // 3.
        Collection<ResultGroup> groups = result.getGroups(se.getSelectedItems(), se.getGroupByColumns());

        // 5.
        List<ResultHeader> headers = result.getSelectedHeaders(selectedItems);
        List<ResultRow> resultRows = new ArrayList<>();
        for (ResultGroup group : groups) {
            resultRows.add(group.getSelectedValues(selectedItems, headers));
        }

        return new Result(server, headers, resultRows);
    }


    private Result calcProductOfTables(List<TableReference> tableReferences)
            throws SqlException {

        List<Result> results = new ArrayList<>();
        for (TableReference tr : tableReferences) {
            results.add(this.getDataFromTableRef(tr));
        }
        if (results.isEmpty()) {
            return new Result(server, Collections.emptyList(), Collections.emptyList());
        }
        Iterator<Result> it = results.iterator();
        Result resultSet = it.next();
        while (it.hasNext()) {
            resultSet = resultSet.productWith(it.next());
        }
        return resultSet;
    }


    private Result getDataFromPersistentTable(DatabaseTableReference dtr)
            throws SqlException {
        PersistentTable table = server.getTable(dtr);
        return new Result(server, table.getResultHeaders(), table.getResultRows());
    }

    private Result getDataFromJoinedTable(JoinedTableReference tableReference)
            throws SqlException {

        Result left =
                this.getDataFromTableRef(tableReference.getLeftTableReference());
        Result right =
                this.getDataFromTableRef(tableReference.getRightTableReference());
        if (tableReference instanceof InnerJoinTableReference) {
            return left.innerJoin(right, tableReference.getPredicate());
        }
        if (tableReference instanceof LeftOuterJoinTableReference) {
            return left.leftOuterJoin(right, tableReference.getPredicate());
        }
        if (tableReference instanceof RightOuterJoinTableReference) {
            return left.rightOuterJoin(right, tableReference.getPredicate());
        }
        throw new UnsupportedTableReferenceTypeException(tableReference);
    }


    private @NotNull Result getDataFromTableRef(
            TableReference tableReference) throws
            SqlException {

        if (tableReference instanceof DatabaseTableReference) {
            return this.getDataFromPersistentTable(
                    (DatabaseTableReference) tableReference);
        }
        if (tableReference instanceof JoinedTableReference) {
            return this.getDataFromJoinedTable((JoinedTableReference) tableReference);
        }
        if (tableReference instanceof TableFromSelectReference) {
            return this.getSubqueryResult(
                    (TableFromSelectReference) tableReference);
        }
        throw new UnsupportedTableReferenceTypeException(tableReference);
    }


    private Result getSubqueryResult(TableFromSelectReference tr)
            throws SqlException {

        // first, get a result of subquery.
        Result result = getInternalQueryResult(tr.getSelectQuery());
        String alias = tr.getAlias();
        if (alias.isEmpty()) {
            return result;
        }

        // second, replace table name with the alias everywhere in the result data.
        List<ResultHeader> newColumns = new ArrayList<>();
        for (ResultHeader cr : result.getColumns()) {
            newColumns.add(new ResultHeader("", alias,
                    cr.getColumnName()));
        }
        List<ResultRow> newRows = new ArrayList<>();
        for (ResultRow row : result.getRows()) {
            List<ResultValue> values = new ArrayList<>();
            for (ResultValue value : row.getValues()) {
                ResultHeader oldHeader = value.getHeader();
                ResultHeader header = new ResultHeader("", alias,
                        oldHeader.getColumnName());
                values.add(new ResultValue(header, value.getValue()));
            }
            newRows.add(new ResultRow(values));
        }
        return new Result(server, newColumns, newRows);
    }

    private Result productWith(Result otherSet) {

        List<ResultHeader> newColumns = new ArrayList<>(columns);
        newColumns.addAll(otherSet.getColumns());
        List<ResultRow> newRows = new ArrayList<>();
        for (ResultRow row : rows) {
            for (ResultRow otherRow : otherSet.getRows()) {
                List<ResultValue> newValues = new ArrayList<>(row.getValues());
                newValues.addAll(otherRow.getValues());
                newRows.add(new ResultRow(newValues));
            }
        }
        return new Result(server, newColumns, newRows);
    }

    private Result applyPredicate(Predicate predicate)
            throws SqlException {

        List<ResultRow> newRows = new ArrayList<>();
        for (ResultRow row : rows) {
            if (row.matchPredicate(predicate)) {
                newRows.add(row);
            }
        }
        return new Result(server, columns, newRows);
    }

    private Result innerJoin(Result right, Predicate sc)
            throws SqlException {

        Result result = this.productWith(right);
        return result.applyPredicate(sc);
    }

    private Result leftOuterJoin(Result right, Predicate sc)
            throws SqlException {

        List<ResultHeader> newColumns = new ArrayList<>();
        newColumns.addAll(columns);
        newColumns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow leftRow : this.getRows()) {
            boolean matchFound = false;
            for (ResultRow rightRow : right.getRows()) {
                List<ResultValue> values = new ArrayList<>(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRow(values);
                if (row.matchPredicate(sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<ResultValue> values =
                        new ArrayList<>(leftRow.getValues());
                for (ResultHeader header : right.getColumns()) {
                    values.add(new ResultValue(header, null));
                }
                rows.add(new ResultRow(values));
            }
        }
        return new Result(server, newColumns, rows);
    }

    private Result rightOuterJoin(Result right, Predicate sc)
            throws SqlException {

        List<ResultHeader> newColumns = new ArrayList<>();
        newColumns.addAll(columns);
        newColumns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow rightRow : right.getRows()) {
            boolean matchFound = false;
            for (ResultRow leftRow : this.getRows()) {
                List<ResultValue> values = new ArrayList<>(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRow(values);
                if (row.matchPredicate(sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<ResultValue> values = new ArrayList<>();
                for (ResultHeader header : columns) {
                    values.add(new ResultValue(header, null));
                }
                values.addAll(rightRow.getValues());
                rows.add(new ResultRow(values));
            }
        }
        return new Result(server, newColumns, rows);
    }

    private List<ResultHeader> getSelectedHeaders(List<SelectedItem> selectedItems) {

        if (selectedItems.isEmpty()) {
            return columns;
        }

        List<ResultHeader> newHeaders = new ArrayList<>();
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                DatabaseTableReference tableRef = (DatabaseTableReference) selectedItem;
                for (ResultHeader header : columns) {
                    if (header.getDatabaseName().equals(tableRef.getDatabaseName()) &&
                            header.getTableName().equals(tableRef.getTableName())) {
                        newHeaders.add(header);
                    }
                }
            } else {
                ColumnExpression columnExpression = (ColumnExpression) selectedItem;
                if (!columnExpression.getAlias().isEmpty()) {
                    newHeaders
                            .add(new ResultHeader((columnExpression).getAlias()));
                } else if (columnExpression instanceof ColumnRef) {
                    newHeaders.add(new ResultHeader((ColumnRef) columnExpression));
                } else {
                    newHeaders.add(new ResultHeader(columnExpression.toString()));
                }
            }
        }
        return newHeaders;
    }


    private Result getSelectedValues(List<SelectedItem> selectedItems)
            throws SqlException {

        List<ResultRow> resultRows = new ArrayList<>();
        for (ResultRow row : rows) {
            resultRows.add(row.evaluateSelectedItems(selectedItems));
        }
        return new Result(server, this.getSelectedHeaders(selectedItems), resultRows);
    }

    private QueryResultImpl createOutputResult() {

        List<QueryResultRow> queryResultRows = new ArrayList<>();
        List<String> resultColumns = columns.stream()
                .map(ResultHeader::getColumnName).collect(Collectors.toList());

        for (ResultRow row : rows) {
            List<Object> values = new ArrayList<>();
            for (ResultValue value : row.getValues()) {
                values.add(value.getValue());
            }
            queryResultRows.add(new QueryResultRowImpl(values));
        }
        return new QueryResultImpl(queryResultRows, resultColumns);
    }

    private Collection<ResultGroup> getGroups(Collection<SelectedItem> selectedItems,
                                              Collection<ColumnRef> groupByColumns)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        if (groupByColumns.isEmpty()) {
            for (SelectedItem se : selectedItems) {
                if (se instanceof AggregateFunction) {
                    return Collections.singletonList(new ResultGroup(server, columns, rows));
                }
            }
        }
        Map<List<ResultValue>, List<ResultRow>> map = new HashMap<>();
        for (ResultRow row : rows) {
            List<ResultValue> values = new ArrayList<>();
            if (!groupByColumns.isEmpty()) {
                for (ColumnRef cr : groupByColumns) {
                    values.add(row.evaluateColumnRef(cr));
                }
            } else {
                values = row.getValues();
            }
            List<ResultValue> key = Collections.unmodifiableList(values);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(row);
        }
        List<ResultHeader> headers = new ArrayList<>();
        for (ColumnRef cr : groupByColumns) {
            headers.add(new ResultHeader(cr));
        }
        if (headers.isEmpty()) {
            headers = columns;
        }
        Collection<ResultGroup> groups = new ArrayList<>();
        for (List<ResultRow> groupRows : map.values()) {
            groups.add(new ResultGroup(server, headers, groupRows));
        }
        return groups;
    }


}
