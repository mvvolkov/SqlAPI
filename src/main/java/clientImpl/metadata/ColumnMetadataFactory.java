package clientImpl.metadata;

public class ColumnMetadataFactory {

    public static ColumnMetadataImpl.Builder<Integer> integerBuilder(
            String columnName) {
        return new ColumnMetadataImpl.Builder<Integer>(columnName, "INTEGER",
                Integer.class);
    }

    public static ColumnMetadataImpl.Builder<String> varcharBuilder(String columnName,
                                                                    int maxLength) {
        return new ColumnMetadataImpl.Builder<String>(columnName, "VARCHAR", String.class,
                maxLength);
    }
}
