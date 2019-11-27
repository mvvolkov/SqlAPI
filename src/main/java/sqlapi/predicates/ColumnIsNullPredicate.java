package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnRef;

public interface ColumnIsNullPredicate extends Predicate {

    @NotNull ColumnRef getColumnRef();

}
