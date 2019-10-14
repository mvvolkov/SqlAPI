package api;

public abstract class SqlInsertableValue<T> {

    private final T value;

    private SqlInsertableValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static class IntegerValue extends SqlInsertableValue<Integer> {

        private IntegerValue(Integer value) {
            super(value);
        }

    }

    public static SqlInsertableValue<Integer> integerValueOf(Integer value) {
        return new IntegerValue(value);
    }

    public static class StringValue extends SqlInsertableValue<String> {

        private StringValue(String value) {
            super(value);
        }
    }

    public static SqlInsertableValue<String> stringValueOf(String value) {
        return new StringValue(value);
    }

    public static class NullValue extends SqlInsertableValue<Object> {
        private NullValue() {
            super(null);
        }
    }

    public static SqlInsertableValue<Object> nullValue(){
        return new NullValue();
    }
}
