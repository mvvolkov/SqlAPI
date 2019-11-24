package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.DeleteQuery;
import sqlapi.predicates.Predicate;

final class DeleteQueryImpl extends AbstractSqlTableQueryImpl
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
}
