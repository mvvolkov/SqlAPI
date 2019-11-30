package localFileDatabase.server.intermediateResult;

import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.metadata.SqlType;

import java.util.HashMap;
import java.util.Map;

public final class SqlValue {

    private final SqlType sqlType;

    private final Object value;

    private static Map<SqlType, Class<?>> allowedClasses = new HashMap<>();

    static {
        allowedClasses.put(SqlType.INTEGER, Integer.class);
        allowedClasses.put(SqlType.VARCHAR, String.class);
    }


    public SqlValue(SqlType sqlType, Object value) {
        this.sqlType = sqlType;
        this.value = value;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public Object getValue() {
        return value;
    }


    public static int getComparisonResult(SqlValue left, SqlValue right)
            throws WrongValueTypeException {
        SqlType sqlType1 = left.getSqlType() == null ? getSqlType(left.getValue()) :
                left.getSqlType();
        SqlType sqlType2 = right.getSqlType() == null ? getSqlType(right.getValue()) :
                right.getSqlType();
        if (sqlType1 != sqlType2) {
            throw new WrongValueTypeException();
        }
        if (sqlType1 == SqlType.INTEGER) {
            Integer i1 = (Integer) left.getValue();
            Integer i2 = (Integer) right.getValue();
            return i1.compareTo(i2);
        }
        if (sqlType1 == SqlType.VARCHAR) {
            String s1 = (String) left.getValue();
            String s2 = (String) right.getValue();
            return s1.compareTo(s2);
        }
        throw new WrongValueTypeException();
    }


    public static SqlType getSqlType(Object value) throws WrongValueTypeException {
        if (value == null) {
            return null;
        }
        for (Map.Entry<SqlType, Class<?>> entry : allowedClasses.entrySet()) {
            Class<?> cl = entry.getValue();
            if (cl.isInstance(value)) {
                return entry.getKey();
            }
        }
        throw new WrongValueTypeException();
    }
}
