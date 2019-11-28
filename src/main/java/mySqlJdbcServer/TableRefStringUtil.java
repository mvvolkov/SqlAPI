package mySqlJdbcServer;

import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.UnsupportedTableReferenceTypeException;
import sqlapi.tables.*;

public class TableRefStringUtil {

    private TableRefStringUtil() {
    }

    public static String getTableReferenceString(TableReference tableReference) throws SqlException {

        if (tableReference instanceof DatabaseTableReference) {
            return getDatabaseTableReferenceString((DatabaseTableReference) tableReference);
        }
        if (tableReference instanceof JoinedTableReference) {
            return getJoinedTableReferenceString((JoinedTableReference) tableReference);
        }
        if (tableReference instanceof TableFromSelectReference) {
            return getTableFromSelectReferenceString((TableFromSelectReference) tableReference);
        }
        throw new UnsupportedTableReferenceTypeException(tableReference);
    }

    public static String getDatabaseTableReferenceString(DatabaseTableReference tableReference) {
        return tableReference.getDatabaseName() + "." + tableReference.getTableName();
    }


    public static String getJoinedTableReferenceString(JoinedTableReference tableReference)
            throws SqlException {
        StringBuilder sb = new StringBuilder();
        boolean leftRefIsDbTable = tableReference.getLeftTableReference() instanceof DatabaseTableReference;
        boolean rightRefIsDbTable = tableReference.getRightTableReference() instanceof DatabaseTableReference;
        if (!leftRefIsDbTable) {
            sb.append("(");
        }
        sb.append(getTableReferenceString(tableReference.getLeftTableReference()));
        if (!leftRefIsDbTable) {
            sb.append(")");
        }
        sb.append(" ");
        sb.append(getJoinedTableOperatorString(tableReference));
        sb.append(" ");
        if (!rightRefIsDbTable) {
            sb.append("(");
        }
        sb.append(getTableReferenceString(tableReference.getRightTableReference()));
        if (!rightRefIsDbTable) {
            sb.append(")");
        }
        if (!tableReference.getPredicate().isEmpty()) {
            sb.append(" ON ");
            sb.append(PredicateStringUtil.getPredicateString(tableReference.getPredicate()));
        }
        return sb.toString();
    }

    private static String getJoinedTableOperatorString(JoinedTableReference tableReference) throws UnsupportedTableReferenceTypeException {
        if (tableReference instanceof InnerJoinTableReference) {
            return "INNER JOIN";
        }
        if (tableReference instanceof LeftOuterJoinTableReference) {
            return "LEFT OUTER JOIN";
        }
        if (tableReference instanceof RightOuterJoinTableReference) {
            return "RIGHT OUTER JOIN";
        }
        throw new UnsupportedTableReferenceTypeException(tableReference);
    }

    public static String getTableFromSelectReferenceString(TableFromSelectReference tableReference) throws SqlException {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(QueryStringUtil.getSelectQueryString(tableReference.getSelectQuery()));
        sb.append(")");
        if (!tableReference.getAlias().isEmpty()) {
            sb.append(" ");
            sb.append(tableReference.getAlias());
        }
        return sb.toString();
    }
}
