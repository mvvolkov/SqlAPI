package api;

public abstract class ColumnExpression {

    private ColumnExpression() {
    }

    public static final class StringLiteral extends ColumnExpression {
        private final String value;

        private StringLiteral(String value) {
            super();
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static ColumnExpression newStringLiteral(String value) {
        return new StringLiteral(value);
    }

    public static final class IntegerLiteral extends ColumnExpression {
        private final Integer value;

        private IntegerLiteral(Integer value) {
            super();
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public static ColumnExpression newIntegerLiteral(Integer value) {
        return new IntegerLiteral(value);
    }

    public static final class Column extends ColumnExpression {
        private final String columnName;

        private Column(String columnName) {
            super();
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

    public static ColumnExpression newColumn(String columnName) {
        return new Column(columnName);
    }

    public static final class Add extends ColumnExpression {
        private final ColumnExpression left;
        private final ColumnExpression right;

        private Add(ColumnExpression left, ColumnExpression right) {
            this.left = left;
            this.right = right;
        }

        public ColumnExpression getLeft() {
            return left;
        }

        public ColumnExpression getRight() {
            return right;
        }
    }

    public static ColumnExpression newAdd(ColumnExpression left, ColumnExpression right) {
        return new Add(left, right);
    }


    public static final class Subtract extends ColumnExpression {
        private final ColumnExpression left;
        private final ColumnExpression right;

        private Subtract(ColumnExpression left, ColumnExpression right) {
            this.left = left;
            this.right = right;
        }

        public ColumnExpression getLeft() {
            return left;
        }

        public ColumnExpression getRight() {
            return right;
        }
    }

    public static ColumnExpression newSubtract(ColumnExpression left, ColumnExpression right) {
        return new Subtract(left, right);
    }

    public static final class Multiply extends ColumnExpression {
        private final ColumnExpression left;
        private final ColumnExpression right;

        private Multiply(ColumnExpression left, ColumnExpression right) {
            this.left = left;
            this.right = right;
        }

        public ColumnExpression getLeft() {
            return left;
        }

        public ColumnExpression getRight() {
            return right;
        }
    }

    public static ColumnExpression newMultiply(ColumnExpression left, ColumnExpression right) {
        return new Multiply(left, right);
    }

    public static final class Divide extends ColumnExpression {
        private final ColumnExpression left;
        private final ColumnExpression right;

        private Divide(ColumnExpression left, ColumnExpression right) {
            this.left = left;
            this.right = right;
        }

        public ColumnExpression getLeft() {
            return left;
        }

        public ColumnExpression getRight() {
            return right;
        }
    }

    public static ColumnExpression newDivide(ColumnExpression left, ColumnExpression right) {
        return new Divide(left, right);
    }
}
