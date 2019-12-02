package localFileDatabase.server.intermediateResult;

import sqlapi.exceptions.WrongValueTypeException;

import java.math.BigDecimal;

public final class DataValue {

    private final DataHeader header;

    private final Object value;

    private static DataValue NULL_VALUE = new DataValue(null);

    public DataValue(DataHeader header, Object value) {
        this.header = header;
        this.value = value;
    }

    public DataValue(Object value) {
        this.header = new DataHeader();
        this.value = value;
    }

    public static DataValue nullValue() {
        return NULL_VALUE;
    }

    public DataHeader getHeader() {
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

    public DataValue add(DataValue anotherDataValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherDataValue.getValue());
        return new DataValue(new DataHeader(), bd1.add(bd2));
    }

    public DataValue subtract(DataValue anotherDataValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherDataValue.getValue());
        return new DataValue(new DataHeader(), bd1.subtract(bd2));
    }

    public DataValue multiply(DataValue anotherDataValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherDataValue.getValue());
        return new DataValue(new DataHeader(), bd1.multiply(bd2));
    }

    public DataValue divide(DataValue anotherDataValue) throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherDataValue.getValue());
        return new DataValue(new DataHeader(), bd1.divide(bd2));
    }

    public int getComparisonResult(Object anotherValue)
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

    public boolean isEqual(Object anotherValue)
            throws WrongValueTypeException {
        if (anotherValue == null && value == null) {
            return true;
        }
        return this.getComparisonResult(anotherValue) == 0;
    }
}
