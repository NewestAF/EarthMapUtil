package com.newestaf.earthmaputil.util;

public class SQLCondition {

    enum ConditionType {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL_TO,
        LESS_THAN_OR_EQUAL_TO,
    }

    private final String column;
    private final ConditionType conditionType;
    private final String value;

    public SQLCondition(String column, String comparison, ConditionType type) {
        this.column = column;
        this.conditionType = type;
        this.value = comparison;
    }

    public String toString() {
        return switch (conditionType) {
            case EQUALS -> column + " = " + value;
            case NOT_EQUALS -> column + " != " + value;
            case GREATER_THAN -> column + " > " + value;
            case LESS_THAN -> column + " < " + value;
            case GREATER_THAN_OR_EQUAL_TO -> column + " >= " + value;
            case LESS_THAN_OR_EQUAL_TO -> column + " <= " + value;
        };
    }


}
