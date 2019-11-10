package clientImpl.metadata;

public class ColumnMetadataFactory {

    public static ColumnMetadataBuilder integerBuilder(
            String columnName) {
        return IntegerColumnMetadataImpl.builder(columnName);
    }

    public static ColumnMetadataBuilder varcharBuilder(String columnName,
                                                       int maxLength) {
        return VarcharColumnMetadataImpl.builder(columnName, maxLength);
    }
}
