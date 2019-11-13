package api.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;

import java.util.List;

public interface ColumnIsNullPredicate extends Predicate {

    ColumnRef getColumnRef();

    @Override
    default Type getType() {
        return Type.IS_NULL;
    }
}
