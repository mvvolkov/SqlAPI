package localFileDatabase.server.intermediate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.exceptions.WrongValueTypeException;

import java.math.BigDecimal;

public final class ResultValue {

    @NotNull
    private final ResultHeader header;

    @Nullable
    private final Object value;

    private static ResultValue NULL_VALUE = new ResultValue(null);

    public ResultValue(@NotNull ResultHeader header, @Nullable Object value) {
        this.header = header;
        this.value = value;
    }

    ResultValue(@Nullable Object value) {
        this.header = new ResultHeader();
        this.value = value;
    }

    public boolean isNull() {
        return value == null;
    }

    static ResultValue nullValue() {
        return NULL_VALUE;
    }

    @NotNull
    ResultHeader getHeader() {
        return header;
    }

    @Nullable
    public Object getValue() {
        return value;
    }

    @Nullable
    private static BigDecimal getBigDecimal(@Nullable Object value)
            throws WrongValueTypeException {
        if (value == null) {
            return null;
        }
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

    @NotNull
    ResultValue add(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        if (bd1 == null || bd2 == null) {
            return ResultValue.nullValue();
        }
        return new ResultValue(new ResultHeader(), bd1.add(bd2));
    }

    @NotNull
    ResultValue subtract(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        if (bd1 == null || bd2 == null) {
            return ResultValue.nullValue();
        }
        return new ResultValue(new ResultHeader(), bd1.subtract(bd2));
    }

    @NotNull
    ResultValue multiply(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        if (bd1 == null || bd2 == null) {
            return ResultValue.nullValue();
        }
        return new ResultValue(new ResultHeader(), bd1.multiply(bd2));
    }

    @NotNull
    ResultValue divide(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherResultValue.getValue());
        if (bd1 == null || bd2 == null) {
            return ResultValue.nullValue();
        }
        return new ResultValue(new ResultHeader(), bd1.divide(bd2));
    }

    int getComparisonResult(@Nullable Object anotherValue)
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

    boolean isEqual(@Nullable Object anotherValue)
            throws WrongValueTypeException {
        if (anotherValue == null && value == null) {
            return true;
        }
        return this.getComparisonResult(anotherValue) == 0;
    }
}
