package api.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;

public interface ColumnValuePredicate extends Predicate {

    ColumnRef getColumnRef();

    ColumnValue getColumnValue();
}
