package clientImpl.stringUtil;

import clientImpl.columnExpr.ColumnExprFactory;
import sqlapi.exceptions.UnsupportedColumnConstraintTypeException;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.TableMetadata;

import java.util.StringJoiner;

public class MetadataStringUtil {

    private MetadataStringUtil() {
    }

    public static String getTableMetadataString(TableMetadata tm)
            throws UnsupportedColumnConstraintTypeException {
        StringJoiner joiner = new StringJoiner(", ");
        for (ColumnMetadata columnMetadata : tm.getColumnsMetadata()) {
            String columnMetadataString = getColumnMetadataString(columnMetadata);
            joiner.add(columnMetadataString);
        }
        String columns = joiner.toString();
        return tm.getTableName() + "(" + columns + ")";
    }

    private static String getColumnMetadataString(ColumnMetadata cm)
            throws UnsupportedColumnConstraintTypeException {
        StringBuilder sb = new StringBuilder();
        for (ColumnConstraint constraint : cm.getConstraints()) {
            if (constraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                sb.insert(0, "(" + constraint.getParameters().get(0) + ")");
                continue;
            }
            sb.append(" ").append(getColumnConstraintString(constraint));
        }

        return cm.getColumnName() + " " + cm.getSqlType() + sb.toString();
    }

    private static String getColumnConstraintString(ColumnConstraint constraint)
            throws UnsupportedColumnConstraintTypeException {
        switch (constraint.getConstraintType()) {
            case MAX_SIZE:
                return "MAX SIZE (" + constraint.getParameters().get(0) + ")";
            case NOT_NULL:
                return "NOT NULL";
            case PRIMARY_KEY:
                return "PRIMARY KEY";
            case DEFAULT_VALUE:
                return "DEFAULT " + ColumnExprStringUtil
                        .getInputValueString(ColumnExprFactory
                                .value(constraint.getParameters().get(0)));
            default:
                throw new UnsupportedColumnConstraintTypeException(
                        constraint.getConstraintType());
        }
    }
}
