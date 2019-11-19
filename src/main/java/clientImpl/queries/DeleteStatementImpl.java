package clientImpl.queries;

import sqlapi.queries.DeleteStatement;
import sqlapi.predicates.Predicate;

public class DeleteStatementImpl extends AbstractSqlStatement implements DeleteStatement {


    private final Predicate predicate;

    public DeleteStatementImpl(String databaseName, String tableName,
                               Predicate predicate) {
        super(databaseName, tableName);
        this.predicate = predicate;
    }


    @Override
    public Predicate getPredicate() {
        return predicate;
    }
}
