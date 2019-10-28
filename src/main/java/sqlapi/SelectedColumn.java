package sqlapi;

public abstract class SelectedColumn {

    private final Type type;

    protected SelectedColumn(Type type) {
        this.type = type;
    }

    public enum Type {
        SELECT_ALL,
        SELECT_ALL_FROM_TABLE,
        SELECT_COLUMN_EXPRESSION
    }

    public abstract String toString();

    public Type getType() {
        return type;
    }

    public static SelectedColumn all() {
        return new SelectAll();
    }

    public static class SelectAll extends SelectedColumn {
        public SelectAll() {
            super(Type.SELECT_ALL);
        }

        @Override
        public String toString() {
            return "*";
        }
    }

    public static class SelectTable extends SelectedColumn {

        private final String table;

        public SelectTable(String table) {
            super(Type.SELECT_ALL_FROM_TABLE);
            this.table = table;
        }

        @Override
        public String toString() {
            return "";
        }
    }

    public static class SelectColumnExpression extends SelectedColumn {

        private final ColumnExpression expression;
        private final String alias;

        public SelectColumnExpression(ColumnExpression expression, String alias) {
            super(Type.SELECT_COLUMN_EXPRESSION);
            this.expression = expression;
            this.alias = alias;
        }


        @Override
        public String toString() {
            return "";
        }
    }
}
