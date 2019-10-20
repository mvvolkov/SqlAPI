package sqlapi;

public class SingleTableJoinOperand extends JoinTableOperand {

    private final String tableName;

    private final String dbName;

    public SingleTableJoinOperand(String tableName, String dbName) {
        this.tableName = tableName;
        this.dbName = dbName;
    }
}
