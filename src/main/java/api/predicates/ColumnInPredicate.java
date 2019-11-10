package api.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;

import java.util.List;

public interface ColumnInPredicate extends Predicate {


    ColumnRef getColumnRef();

    List<ColumnValue> getColumnValues();

    @Override
    default Type getType() {
        return Type.IN;
    }

}
