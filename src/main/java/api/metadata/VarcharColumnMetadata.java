package api.metadata;

public interface VarcharColumnMetadata extends ColumnMetadata<String> {
    int getMaxLength();
}
