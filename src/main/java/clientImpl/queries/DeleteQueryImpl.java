package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;
import sqlapi.queries.DeleteQuery;

import java.util.ArrayList;
import java.util.Arrays;

final class DeleteQueryImpl extends TableQueryImpl
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

    @Override
    public void setParameters(Object... parameters) {
        predicate.setParameters(new ArrayList<>(Arrays.asList(parameters)));
    }
}
