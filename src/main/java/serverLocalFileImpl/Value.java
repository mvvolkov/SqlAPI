package serverLocalFileImpl;

import java.io.Serializable;

public final class Value<T extends Comparable<T> & Serializable> implements Serializable {

    public static final long serialVersionUID = -1974733077624735179L;

    private final T value;

    private final Class<T> type;

    public Value(Class<T> type, Object value) {
        this.type = type;
        this.value = this.type.cast(value);
    }

    public T getValue() {
        return value;
    }

    public boolean isNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
