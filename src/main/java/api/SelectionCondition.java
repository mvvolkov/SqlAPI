package api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SelectionCondition {

    private final SelectionConditionType type;

    private SelectionCondition(SelectionConditionType type) {
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

    public static class EmptySelectionCondition extends SelectionCondition {
        public EmptySelectionCondition() {
            super(SelectionConditionType.EMPTY);
        }
    }

    public static final class SingleSelectionCondition extends SelectionCondition {

        @NotNull
        private final SingleConditionType type;

        @Nullable
        private final Object left;

        @Nullable
        private final Object right;


        private SingleSelectionCondition(@NotNull SingleConditionType type, @Nullable Object left,
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

    public static final class CompoundSelectionCondition extends SelectionCondition {

        @NotNull
        private final CompoundConditionType type;

        @NotNull
        private final SelectionCondition left;

        @NotNull
        private final SelectionCondition right;

        private CompoundSelectionCondition(@NotNull CompoundConditionType type, @NotNull SelectionCondition left, @NotNull SelectionCondition right) {
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


    public static SelectionCondition getEquals(Object left, Object right) {
        return new SingleSelectionCondition(SingleConditionType.EQUALS, left, right);
    }

    public static SelectionCondition getNotEquals(Object left, Object right) {
        return new SingleSelectionCondition(SingleConditionType.NOT_EQUALS, left, right);
    }

    public static SelectionCondition getGreater(Object left, Object right) {
        return new SingleSelectionCondition(SingleConditionType.GREATER, left, right);
    }

    public static SelectionCondition getGreaterOrEquals(Object left, Object right) {
        return new SingleSelectionCondition(SingleConditionType.GREATER_OR_EQUAL, left, right);
    }

    public static SelectionCondition getLess(Object left, Object right) {
        return new SingleSelectionCondition(SingleConditionType.LESS, left, right);
    }

    public static SelectionCondition getLessOrEquals(Object left, Object right) {
        return new SingleSelectionCondition(SingleConditionType.LESS_OR_EQUAL, left, right);
    }

    public static SelectionCondition getAnd(SelectionCondition left, SelectionCondition right) {
        return new CompoundSelectionCondition(CompoundConditionType.AND, left, right);
    }


    public static SelectionCondition getOr(SelectionCondition left, SelectionCondition right) {
        return new CompoundSelectionCondition(CompoundConditionType.OR, left, right);
    }

    public static SelectionCondition getEmpty() {
        return new EmptySelectionCondition();
    }


}
