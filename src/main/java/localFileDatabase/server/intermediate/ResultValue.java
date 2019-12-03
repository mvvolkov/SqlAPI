package localFileDatabase.server.intermediate;

import sqlapi.exceptions.WrongValueTypeException;

import java.math.BigDecimal;

public final class ResultValue {

    private final ResultHeader header;

    private final Object value;

    private static ResultValue NULL_VALUE = new ResultValue(null);

    public ResultValue(ResultHeader header, Object value) {
        this.header = header;
        this.value = value;
    }

    ResultValue(Object value) {
        this.header = new ResultHeader();
        this.value = value;
    }

    public boolean isNull() {
        return value == null;
    }

    static ResultValue nullValue() {
        return NULL_VALUE;
    }

    ResultHeader getHeader() {
        return header;
    }

    public Object getValue() {
        return value;
    }

    private static BigDecimal getBigDecimal(Object value) throws WrongValueTypeException {
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException nfe) {
                throw new WrongValueTypeException();
            }
        }
        if (value instanceof Number) {
            try {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            } catch (NumberFormatException nfe) {
                throw new WrongValueTypeException();
            }
        }
        throw new WrongValueTypeException();
    }

    ResultValue add(ResultValue anotherResultValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        return new ResultValue(new ResultHeader(), bd1.add(bd2));
    }

    ResultValue subtract(ResultValue anotherResultValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        return new ResultValue(new ResultHeader(), bd1.subtract(bd2));
    }

    ResultValue multiply(ResultValue anotherResultValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        return new ResultValue(new ResultHeader(), bd1.multiply(bd2));
    }

    ResultValue divide(ResultValue anotherResultValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        return new ResultValue(new ResultHeader(), bd1.divide(bd2));
    }

    int getComparisonResult(Object anotherValue)
            throws WrongValueTypeException {
        if (value instanceof Number) {
            BigDecimal bd1 = getBigDecimal(value);
            BigDecimal bd2 = getBigDecimal(anotherValue);
            return bd1.compareTo(bd2);
        } else if (value instanceof String) {
            if (!(anotherValue instanceof String)) {
                throw new WrongValueTypeException();
            }
            String s1 = (String) value;
            String s2 = (String) anotherValue;
            return s1.compareTo(s2);
        }
        throw new WrongValueTypeException();
    }

    boolean isEqual(Object anotherValue)
            throws WrongValueTypeException {
        if (anotherValue == null && value == null) {
            return true;
        }
        return this.getComparisonResult(anotherValue) == 0;
    }
}
