package sqlapi.predicates;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.ColumnValue;

import java.util.List;

public interface ColumnInPredicate extends Predicate {


    ColumnRef getColumnRef();

    List<ColumnValue> getColumnValues();

    @Override
    default Type getType() {
        return Type.IN;
    }

}
