package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.InputValue;

import java.util.List;

public interface ColumnInPredicate extends Predicate {


    @NotNull ColumnRef getColumnRef();

    @NotNull List<InputValue> getColumnValues();

}
