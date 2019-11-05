package clientDefaultImpl;

import api.selectionPredicate.Predicate;
import org.jetbrains.annotations.NotNull;
import api.ColumnReference;

import java.util.List;

public class SelectionPredicateImpl implements Predicate {

    @NotNull
    public final Type type;

    protected SelectionPredicateImpl(@NotNull Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }


    protected static String getSqlString(Object value) {
        return (value instanceof String) ? "'" + value + "'" : String.valueOf(value);
    }

    //    /**
//     * All descendants must implement toString method.
//     *
//     * @return
//     */
//    public abstract String toString();
//
    protected String getOperatorString() {
        switch (type) {
            case IN:
                return "IN";
            case IS_NULL:
                return "IS NULL";
            case IS_NOT_NULL:
                return "IS NOT NULL";
            case EQUALS:
                return "=";
            case NOT_EQUALS:
                return "!=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_EQUALS:
                return ">=";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUALS:
                return "<=";
            case AND:
                return "AND";
            case OR:
                return "OR";
            default:
                return "";
        }
    }

}
