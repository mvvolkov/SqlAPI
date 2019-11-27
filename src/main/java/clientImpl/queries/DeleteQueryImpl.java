package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.DeleteQuery;
import sqlapi.predicates.Predicate;

final class DeleteQueryImpl extends TableActionQueryImpl
        implements DeleteQuery {


    private final @NotNull Predicate predicate;

    DeleteQueryImpl(@NotNull String databaseName, @NotNull String tableName,
                    @NotNull Predicate predicate) {
        super(databaseName, tableName);
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(this.getDatabaseName());
        sb.append(".");
        sb.append(this.getTableName());
        if (!predicate.isEmpty()) {
            sb.append(" WHERE ").append(predicate);
        }
        sb.append(";");
        return sb.toString();
    }
}
