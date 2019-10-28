package sqlapi.selectionResult;

public interface ResultValue {
    String getColumnName();

    Integer getInteger();

    String getString();

    <T> Comparable<T> getValue();

    boolean isNull();

    boolean isNotNull();
}
