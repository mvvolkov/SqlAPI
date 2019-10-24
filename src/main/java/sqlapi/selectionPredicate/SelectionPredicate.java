package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;
import sqlapi.ColumnReference;

import java.io.Serializable;
import java.util.List;

public abstract class SelectionPredicate implements Serializable {

    @NotNull
    public final Type type;

    protected SelectionPredicate(@NotNull Type type) {
        this.type = type;
    }

    public enum Type {
        EMPTY,
        IS_NULL,
        IS_NOT_NULL,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS,
        AND,
        OR,
        IN
    }

    protected static String getSqlString(Object value) {
        return (value instanceof String) ? "'" + value + "'" : String.valueOf(value);
    }

    /**
     * All descendants must implement toString method.
     *
     * @return
     */
    public abstract String toString();

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

    public Type getType() {
        return type;
    }

    // Static methods for creating new selection criteria.

    public static SelectionPredicate empty() {
        return new EmptyPredicate();
    }

    public static SelectionPredicate isNull(ColumnReference columnReference) {
        return new ColumnIsNullPredicate(Type.IS_NULL, columnReference);
    }

    public static SelectionPredicate isNotNull(ColumnReference columnReference) {
        return new ColumnIsNullPredicate(Type.IS_NOT_NULL, columnReference);
    }

    public static SelectionPredicate and(SelectionPredicate left, SelectionPredicate right) {
        return new CombinedPredicate(Type.AND, left, right);
    }

    public static SelectionPredicate or(SelectionPredicate left, SelectionPredicate right) {
        return new CombinedPredicate(Type.OR, left, right);
    }

    public static SelectionPredicate equals(ColumnReference columnReference, Object value) {
        return new ColumnComparisonPredicate(Type.EQUALS, columnReference, value);
    }

    public static SelectionPredicate notEquals(ColumnReference columnReference, Object value) {
        return new ColumnComparisonPredicate(Type.NOT_EQUALS, columnReference, value);
    }

    public static SelectionPredicate greaterThan(ColumnReference columnReference, Object value) {
        return new ColumnComparisonPredicate(Type.GREATER_THAN, columnReference, value);
    }

    public static SelectionPredicate greaterThanOrEquals(ColumnReference columnReference, Object value) {
        return new ColumnComparisonPredicate(Type.GREATER_THAN_OR_EQUALS, columnReference, value);
    }

    public static SelectionPredicate lessThan(ColumnReference columnReference, Object value) {
        return new ColumnComparisonPredicate(Type.LESS_THAN, columnReference, value);
    }

    public static SelectionPredicate lessThanOrEquals(ColumnReference columnReference, Object value) {
        return new ColumnComparisonPredicate(Type.LESS_THAN_OR_EQUALS, columnReference, value);
    }

    public static SelectionPredicate in(ColumnReference columnReference, List<?> values) {
        return new ColumnInPredicate(columnReference, values);
    }


    public boolean isEmpty() {
        return type == Type.EMPTY;
    }


    public boolean isNotEmpty() {
        return !this.isEmpty();
    }


}
