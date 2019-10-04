package api;

import org.jetbrains.annotations.NotNull;


// Use Builder here!!! Or may be not!
public abstract class SqlColumnDescription {

    @NotNull
    private final String name;

    @NotNull
    private final SqlColumnType type;

    private final boolean isNullable;

    private final boolean isPrimaryKey;


    private SqlColumnDescription(@NotNull String name, @NotNull SqlColumnType type, boolean isNullable, boolean isPrimaryKey) {
        this.name = name;
        this.type = type;
        this.isNullable = isNullable;
        this.isPrimaryKey = isPrimaryKey;
    }


    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public SqlColumnType getType() {
        return type;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public static final class IntegerColumn extends SqlColumnDescription {

        private IntegerColumn(@NotNull String name, boolean isNullable, boolean isPrimaryKey) {
            super(name, SqlColumnType.INTEGER, isNullable, isPrimaryKey);
        }
    }

    public static final class VarcharColumn extends SqlColumnDescription {

        private final int maxLength;

        private VarcharColumn(@NotNull String name, boolean isNullable, boolean isPrimaryKey, int maxLength) {
            super(name, SqlColumnType.VARCHAR, isNullable, isPrimaryKey);
            this.maxLength = maxLength;
        }

        public int getMaxLength() {
            return maxLength;
        }
    }

    public static SqlColumnDescription newInteger(String name, boolean isNullable, boolean isPrimaryKey) {
        return new IntegerColumn(name, isNullable, isPrimaryKey);
    }

    public static SqlColumnDescription newVarchar(String name, boolean isNullable, boolean isPrimaryKey, int maxLength) {
        return new VarcharColumn(name, isNullable, isPrimaryKey, maxLength);
    }


}
