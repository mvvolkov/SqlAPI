package api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SqlSelectionCondition {

    private final SelectionConditionType type;

    private SqlSelectionCondition(SelectionConditionType type) {
        this.type = type;
    }

    public enum SelectionConditionType {
        EMPTY,
        SINGLE,
        COMPOUND
    }

    public enum SingleConditionType {
        EQUALS,
        NOT_EQUALS,
        GREATER,
        GREATER_OR_EQUAL,
        LESS,
        LESS_OR_EQUAL
    }

    public enum CompoundConditionType {
        AND,
        OR
    }

    public SelectionConditionType getType() {
        return type;
    }

    public static class EmptySqlSelectionCondition extends SqlSelectionCondition {
        public EmptySqlSelectionCondition() {
            super(SelectionConditionType.EMPTY);
        }
    }

    public boolean isEmpty() {
        return type == SelectionConditionType.EMPTY;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public static final class SingleSqlSelectionCondition extends SqlSelectionCondition {

        @NotNull
        private final SingleConditionType type;

        @Nullable
        private final Object left;

        @Nullable
        private final Object right;


        private SingleSqlSelectionCondition(@NotNull SingleConditionType type, @Nullable Object left,
                                            @Nullable Object right) {
            super(SelectionConditionType.SINGLE);
            this.type = type;
            this.left = left;
            this.right = right;
        }

        @NotNull
        public SingleConditionType getSingleConditionType() {
            return type;
        }

        @Nullable
        public Object getLeft() {
            return left;
        }

        @Nullable
        public Object getRight() {
            return right;
        }
    }

    public static final class CompoundSqlSelectionCondition extends SqlSelectionCondition {

        @NotNull
        private final CompoundConditionType type;

        @NotNull
        private final SqlSelectionCondition left;

        @NotNull
        private final SqlSelectionCondition right;

        private CompoundSqlSelectionCondition(@NotNull CompoundConditionType type, @NotNull SqlSelectionCondition left, @NotNull SqlSelectionCondition right) {
            super(SelectionConditionType.COMPOUND);
            this.type = type;
            this.left = left;
            this.right = right;
        }

        @NotNull
        public CompoundConditionType getCompoundConditionType() {
            return type;
        }
    }


    public static SqlSelectionCondition getEquals(Object left, Object right) {
        return new SingleSqlSelectionCondition(SingleConditionType.EQUALS, left, right);
    }

    public static SqlSelectionCondition getNotEquals(Object left, Object right) {
        return new SingleSqlSelectionCondition(SingleConditionType.NOT_EQUALS, left, right);
    }

    public static SqlSelectionCondition getGreater(Object left, Object right) {
        return new SingleSqlSelectionCondition(SingleConditionType.GREATER, left, right);
    }

    public static SqlSelectionCondition getGreaterOrEquals(Object left, Object right) {
        return new SingleSqlSelectionCondition(SingleConditionType.GREATER_OR_EQUAL, left, right);
    }

    public static SqlSelectionCondition getLess(Object left, Object right) {
        return new SingleSqlSelectionCondition(SingleConditionType.LESS, left, right);
    }

    public static SqlSelectionCondition getLessOrEquals(Object left, Object right) {
        return new SingleSqlSelectionCondition(SingleConditionType.LESS_OR_EQUAL, left, right);
    }

    public static SqlSelectionCondition getAnd(SqlSelectionCondition left, SqlSelectionCondition right) {
        return new CompoundSqlSelectionCondition(CompoundConditionType.AND, left, right);
    }


    public static SqlSelectionCondition getOr(SqlSelectionCondition left, SqlSelectionCondition right) {
        return new CompoundSqlSelectionCondition(CompoundConditionType.OR, left, right);
    }

    public static SqlSelectionCondition getEmpty() {
        return new EmptySqlSelectionCondition();
    }


}
