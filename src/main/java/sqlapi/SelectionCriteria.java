package sqlapi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SelectionCriteria {

    protected final Type type;

    protected SelectionCriteria(Type type) {
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
        String result = null;
        if (value instanceof String) {
            result = "'" + value + "'";
        } else {
            result = String.valueOf(value);
        }
        return result;
    }

    /**
     * All descendants must implement toString method.
     *
     * @return
     */
    public abstract String toString();

    protected String getTypeString() {
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

    public static SelectionCriteria empty() {
        return new EmptySelectionCriteria();
    }

    public static SelectionCriteria isNull(ColumnReference columnReference) {
        return new IsNullCriteria(Type.IS_NULL, columnReference);
    }

    public static SelectionCriteria isNotNull(ColumnReference columnReference) {
        return new IsNullCriteria(Type.IS_NOT_NULL, columnReference);
    }

    public static SelectionCriteria and(SelectionCriteria left, SelectionCriteria right) {
        return new CombinedPredicate(Type.AND, left, right);
    }

    public static SelectionCriteria or(SelectionCriteria left, SelectionCriteria right) {
        return new CombinedPredicate(Type.OR, left, right);
    }

    public static SelectionCriteria equals(Object left, Object right) {
        return new BinaryPredicate(Type.EQUALS, left, right);
    }

    public static SelectionCriteria notEquals(Object left, Object right) {
        return new BinaryPredicate(Type.NOT_EQUALS, left, right);
    }

    public static SelectionCriteria greaterThan(Object left, Object right) {
        return new BinaryPredicate(Type.GREATER_THAN, left, right);
    }

    public static SelectionCriteria greaterThanOrEquals(Object left, Object right) {
        return new BinaryPredicate(Type.GREATER_THAN_OR_EQUALS, left, right);
    }

    public static SelectionCriteria lessThan(Object left, Object right) {
        return new BinaryPredicate(Type.LESS_THAN, left, right);
    }

    public static SelectionCriteria lessThanOrEquals(Object left, Object right) {
        return new BinaryPredicate(Type.LESS_THAN_OR_EQUALS, left, right);
    }

    public static SelectionCriteria in(ColumnReference columnReference, List<?> values) {
        return new InCriteria(columnReference, values);
    }


    /**
     * Checks if the selection criteria are empty
     *
     * @return
     */
    public boolean isEmpty() {
        return type == Type.EMPTY;
    }

    /**
     * Checks if the selection criteria are not empty
     *
     * @return
     */
    public boolean isNotEmpty() {
        return !this.isEmpty();
    }


    public static final class BinaryPredicate extends SelectionCriteria {

        /**
         * Left operand of the binary expression.
         */
        @NotNull
        private final Object left;

        /**
         * Right operand of the binary expression.
         */
        @NotNull
        private final Object right;

        /**
         * Instance of this type can be obtained with static method only.
         *
         * @param type  - binary operation type.
         * @param left  - left operand.
         * @param right - right operand.
         */
        private BinaryPredicate(Type type, Object left, Object right) {
            super(type);
            this.left = left;
            this.right = right;
        }


        @Override
        public String toString() {
            return getSqlString(left) + " " + this.getTypeString() + " " + getSqlString(right);
        }
    }

    public static final class CombinedPredicate extends SelectionCriteria {

        /**
         * Left predicate.
         */
        protected final SelectionCriteria left;

        /**
         * Right predicate.
         */
        protected final SelectionCriteria right;

        /**
         * Instance of this type can be obtained with static method only.
         *
         * @param type  - binary operation type.
         * @param left  - left predicate.
         * @param right - right predicate.
         */
        private CombinedPredicate(Type type, SelectionCriteria left, SelectionCriteria right) {
            super(type);
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left + ") " + this.getTypeString() + " (" + right + ")";
        }
    }

    /**
     * This class should be used in case when selection criteria is not specified.
     */
    public static class EmptySelectionCriteria extends SelectionCriteria {

        public EmptySelectionCriteria() {
            super(Type.EMPTY);
        }

        @Override
        public String toString() {
            return "";
        }
    }

    /**
     * The predicate for checking whether the column value is NULL or NOT NULL.
     */
    public static class IsNullCriteria extends SelectionCriteria {

        private final ColumnReference columnReference;

        private IsNullCriteria(Type type, ColumnReference columnReference) {
            super(type);
            this.columnReference = columnReference;
        }

        @Override
        public String toString() {
            return columnReference + " " + this.getTypeString();
        }
    }

    public static class InCriteria extends SelectionCriteria {

        private final ColumnReference columnReference;

        private final List<?> values;

        public InCriteria(ColumnReference columnReference, List<?> values) {
            super(Type.IN);
            this.columnReference = columnReference;
            this.values = values;
        }


        @Override
        public String toString() {
            return columnReference + " " + this.getTypeString() + " (" + values.stream()
                    .map(obj -> getSqlString(obj)).collect(Collectors.joining(", "))
                    + ")";
        }
    }
}
