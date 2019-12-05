package localFileDatabase.server.intermediate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.exceptions.InvalidQueryException;
import sqlapi.exceptions.WrongValueTypeException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ResultValue {

    @NotNull
    private final ResultHeader header;

    @Nullable
    private final Object value;

    private static final ResultValue NULL_VALUE = new ResultValue(null);

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

    @NotNull
    private static BigDecimal getBigDecimal(@NotNull Object value)
            throws WrongValueTypeException {
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException nfe) {
                throw new WrongValueTypeException(BigDecimal.class.getSimpleName(),
                        value);
            }
        }
        if (value instanceof Number) {
            try {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            } catch (NumberFormatException nfe) {
                throw new WrongValueTypeException(BigDecimal.class.getSimpleName(),
                        value);
            }
        }
        throw new WrongValueTypeException(BigDecimal.class.getSimpleName(), value);
    }

    @NotNull
    ResultValue add(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        Object anotherValue = anotherResultValue.getValue();
        if (value == null || anotherValue == null) {
            return ResultValue.nullValue();
        }
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherValue);
        return new ResultValue(new ResultHeader(), bd1.add(bd2));
    }

    @NotNull
    ResultValue subtract(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        Object anotherValue = anotherResultValue.getValue();
        if (value == null || anotherValue == null) {
            return ResultValue.nullValue();
        }
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherValue);
        return new ResultValue(new ResultHeader(), bd1.subtract(bd2));
    }

    @NotNull
    ResultValue multiply(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        Object anotherValue = anotherResultValue.getValue();
        if (value == null || anotherValue == null) {
            return ResultValue.nullValue();
        }
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherValue);
        return new ResultValue(new ResultHeader(), bd1.multiply(bd2));
    }

    @NotNull
    ResultValue divide(@NotNull ResultValue anotherResultValue)
            throws WrongValueTypeException {
        Object anotherValue = anotherResultValue.getValue();
        if (value == null || anotherValue == null) {
            return ResultValue.nullValue();
        }
        BigDecimal bd1 = getBigDecimal(value);
        BigDecimal bd2 = getBigDecimal(anotherValue);
        // The scele = 4 is used here to match the result of MySQL server.
        return new ResultValue(new ResultHeader(),
                bd1.divide(bd2, 4, RoundingMode.FLOOR));
    }

    int getComparisonResult(@NotNull Object anotherValue)
            throws WrongValueTypeException, InvalidQueryException {
        if (value instanceof Number) {
            BigDecimal bd1 = getBigDecimal(value);
            BigDecimal bd2 = getBigDecimal(anotherValue);
            return bd1.compareTo(bd2);
        } else if (value instanceof String) {
            if (!(anotherValue instanceof String)) {
                throw new WrongValueTypeException(String.class.getSimpleName(),
                        anotherValue);
            }
            String s1 = (String) value;
            String s2 = (String) anotherValue;
            return s1.compareTo(s2);
        }
        throw new InvalidQueryException(
                "Value " + value + " can not be compared to other values.");
    }

    boolean isEqual(@Nullable Object anotherValue)
            throws WrongValueTypeException, InvalidQueryException {
        if (anotherValue == null) {
            return value == null;
        }
        return this.getComparisonResult(anotherValue) == 0;
    }
}
