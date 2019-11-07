package api;

public interface VarcharColumnMetadata extends ColumnMetadata<String> {
    int getMaxLength();
}
