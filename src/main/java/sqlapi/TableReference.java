package sqlapi;

public interface TableReference {

    default boolean isBaseTable() {
        return false;
    }

    default boolean isInnerJoin() {
        return false;
    }

    default boolean isLeftOuterJoin() {
        return false;
    }

    default boolean isRightOuterJoin() {
        return false;
    }

    default boolean isSelectExpression() {
        return false;
    }
}
