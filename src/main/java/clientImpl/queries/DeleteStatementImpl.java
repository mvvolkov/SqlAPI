package clientImpl.queries;

import api.queries.DeleteStatement;
import api.predicates.Predicate;

public class DeleteStatementImpl extends AbstractSqlStatement implements DeleteStatement {


    private final Predicate predicate;

    public DeleteStatementImpl(String schemaName, String tableName,
                               Predicate predicate) {
        super(tableName);
        this.predicate = predicate;
    }


    @Override
    public Predicate getPredicate() {
        return predicate;
    }
}
