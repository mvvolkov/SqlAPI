package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnRef;

public interface ColumnIsNotNullPredicate extends Predicate {

    @NotNull ColumnRef getColumnRef();

}
