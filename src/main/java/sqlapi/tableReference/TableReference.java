package sqlapi.tableReference;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.selectionPredicate.SelectionPredicate;

public abstract class TableReference {

    @Nullable
    protected final String alias;

    protected TableReference(@Nullable String alias) {
        this.alias = alias;
    }

    public static TableReference baseTable(@NotNull String tableName, @NotNull String dbName) {
        return new BaseTableReference(tableName, dbName);
    }

    public static TableReference baseTable(@NotNull String tableName, @NotNull String dbName, String alias) {
        return new BaseTableReference(tableName, dbName, alias);
    }

    public static TableReference innerJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableReference(JoinTableReference.Type.INNER_JOIN, left, right, selectionPredicate);
    }

    public static TableReference leftOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableReference(JoinTableReference.Type.LEFT_OUTER_JOIN, left, right, selectionPredicate);
    }

    public static TableReference rightOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableReference(JoinTableReference.Type.RIGHT_OUTER_JOIN, left, right, selectionPredicate);
    }
}
