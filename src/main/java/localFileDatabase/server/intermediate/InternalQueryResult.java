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

public final class InternalQueryResult {


    @NotNull
    private final LocalFileDbServer server;

    @NotNull
    private final List<ResultHeader> headers;

    @NotNull
    private final Collection<ResultRow> rows;

    public InternalQueryResult(@NotNull LocalFileDbServer server) {
        this(server, Collections.emptyList(), Collections.emptyList());
    }

    private InternalQueryResult(@NotNull LocalFileDbServer server,
                                @NotNull List<ResultHeader> headers,
                                @NotNull Collection<ResultRow> rows) {
        this.server = server;
        this.headers = Collections.unmodifiableList(headers);
        this.rows = Collections.unmodifiableCollection(rows);
    }

    @NotNull
    public List<ResultHeader> getHeaders() {
        return headers;
    }

    @NotNull
    public Collection<ResultRow> getRows() {
        return rows;
    }

    @NotNull
    public QueryResultImpl getQueryResult(@NotNull SelectQuery selectQuery)
            throws SqlException {
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
    @NotNull
    private InternalQueryResult getInternalQueryResult(@NotNull SelectQuery se) throws
            SqlException {

        // 1.
        InternalQueryResult result = this.calcProductOfTables(se.getTableReferences());

        // 2.
        result = result.applyPredicate(se.getPredicate());

        List<SelectedItem> selectedItems = se.getSelectedItems();

        // 3.
        Collection<ResultGroup> groups =
                result.getGroups(se.getSelectedItems(), se.getGroupByColumns());

        // 4.
        // to be done.

        // 5.
        List<ResultHeader> headers = result.getSelectedHeaders(selectedItems);
        List<ResultRow> resultRows = new ArrayList<>();
        for (ResultGroup group : groups) {
            resultRows.add(group.getSelectedValues(selectedItems, headers));
        }

        // 6,7,8
        // to be done.

        return new InternalQueryResult(server, headers, resultRows);
    }

    @NotNull
    private InternalQueryResult calcProductOfTables(
            @NotNull List<TableReference> tableReferences)
            throws SqlException {

        List<InternalQueryResult> results = new ArrayList<>();
        for (TableReference tr : tableReferences) {
            results.add(this.getDataFromTableRef(tr));
        }
        if (results.isEmpty()) {
            return new InternalQueryResult(server, Collections.emptyList(),
                    Collections.emptyList());
        }
        Iterator<InternalQueryResult> it = results.iterator();
        InternalQueryResult resultSet = it.next();
        while (it.hasNext()) {
            resultSet = resultSet.productWith(it.next());
        }
        return resultSet;
    }


    @NotNull
    private InternalQueryResult getDataFromPersistentTable(
            @NotNull DatabaseTableReference dtr)
            throws SqlException {
        PersistentTable table = server.getTable(dtr);
        return new InternalQueryResult(server, table.getResultHeaders(),
                table.getResultRows());
    }

    @NotNull
    private InternalQueryResult getDataFromJoinedTable(
            @NotNull JoinedTableReference tableReference)
            throws SqlException {

        InternalQueryResult left =
                this.getDataFromTableRef(tableReference.getLeftTableReference());
        InternalQueryResult right =
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

    @NotNull
    private InternalQueryResult getDataFromTableRef(
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


    @NotNull
    private InternalQueryResult getSubqueryResult(@NotNull TableFromSelectReference tr)
            throws SqlException {

        // first, get a result of subquery.
        InternalQueryResult result = getInternalQueryResult(tr.getSelectQuery());
        String alias = tr.getAlias();
        if (alias.isEmpty()) {
            return result;
        }

        // second, replace table name with the alias everywhere in the result data.
        List<ResultHeader> newColumns = new ArrayList<>();
        for (ResultHeader cr : result.getHeaders()) {
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
        return new InternalQueryResult(server, newColumns, newRows);
    }

    @NotNull
    private InternalQueryResult productWith(@NotNull InternalQueryResult otherSet) {

        List<ResultHeader> newColumns = new ArrayList<>(headers);
        newColumns.addAll(otherSet.getHeaders());
        List<ResultRow> newRows = new ArrayList<>();
        for (ResultRow row : rows) {
            for (ResultRow otherRow : otherSet.getRows()) {
                List<ResultValue> newValues = new ArrayList<>(row.getValues());
                newValues.addAll(otherRow.getValues());
                newRows.add(new ResultRow(newValues));
            }
        }
        return new InternalQueryResult(server, newColumns, newRows);
    }

    @NotNull
    private InternalQueryResult applyPredicate(@NotNull Predicate predicate)
            throws SqlException {

        List<ResultRow> newRows = new ArrayList<>();
        for (ResultRow row : rows) {
            if (row.matchPredicate(predicate)) {
                newRows.add(row);
            }
        }
        return new InternalQueryResult(server, headers, newRows);
    }

    @NotNull
    private InternalQueryResult innerJoin(@NotNull InternalQueryResult right,
                                          @NotNull Predicate sc)
            throws SqlException {

        return this.productWith(right).applyPredicate(sc);
    }

    @NotNull
    private InternalQueryResult leftOuterJoin(@NotNull InternalQueryResult right,
                                              @NotNull Predicate sc)
            throws SqlException {

        List<ResultHeader> newColumns = new ArrayList<>();
        newColumns.addAll(headers);
        newColumns.addAll(right.getHeaders());
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
                for (ResultHeader header : right.getHeaders()) {
                    values.add(new ResultValue(header, null));
                }
                rows.add(new ResultRow(values));
            }
        }
        return new InternalQueryResult(server, newColumns, rows);
    }

    @NotNull
    private InternalQueryResult rightOuterJoin(@NotNull InternalQueryResult right,
                                               @NotNull Predicate sc)
            throws SqlException {

        List<ResultHeader> newColumns = new ArrayList<>();
        newColumns.addAll(headers);
        newColumns.addAll(right.getHeaders());
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
                for (ResultHeader header : headers) {
                    values.add(new ResultValue(header, null));
                }
                values.addAll(rightRow.getValues());
                rows.add(new ResultRow(values));
            }
        }
        return new InternalQueryResult(server, newColumns, rows);
    }

    @NotNull
    private List<ResultHeader> getSelectedHeaders(
            @NotNull List<SelectedItem> selectedItems) {

        if (selectedItems.isEmpty()) {
            return headers;
        }

        List<ResultHeader> newHeaders = new ArrayList<>();
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                DatabaseTableReference tableRef = (DatabaseTableReference) selectedItem;
                for (ResultHeader header : headers) {
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


    @NotNull
    private QueryResultImpl createOutputResult() {

        List<QueryResultRow> queryResultRows = new ArrayList<>();
        List<String> resultColumns = headers.stream()
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

    @NotNull
    private Collection<ResultGroup> getGroups(
            @NotNull Collection<SelectedItem> selectedItems,
            @NotNull Collection<ColumnRef> groupByColumns)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        if (groupByColumns.isEmpty()) {
            for (SelectedItem se : selectedItems) {
                if (se instanceof AggregateFunction) {
                    List<ResultHeader> headers = new ArrayList<>();
                    for (ResultHeader header : this.headers) {
                        headers.add(new ResultHeader(header.getColumnName()));
                    }
                    return Collections
                            .singletonList(
                                    new ResultGroup(server, Collections.emptyList(),
                                            rows));
                }
            }
        }
        Map<List<Object>, List<ResultRow>> map = new HashMap<>();
        for (ResultRow row : rows) {
            List<Object> values = new ArrayList<>();
            if (!groupByColumns.isEmpty()) {
                for (ColumnRef cr : groupByColumns) {
                    values.add(row.evaluateColumnRef(cr).getValue());
                }
            } else {
                for (ResultValue resultValue : row.getValues()) {
                    values.add(resultValue.getValue());
                }
            }
            List<Object> key = Collections.unmodifiableList(values);
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
            for (ResultHeader header : this.headers) {
                headers.add(new ResultHeader(header.getColumnName()));
            }
        }
        Collection<ResultGroup> groups = new ArrayList<>();
        for (List<ResultRow> groupRows : map.values()) {
            groups.add(new ResultGroup(server, headers, groupRows));
        }
        return groups;
    }
}
