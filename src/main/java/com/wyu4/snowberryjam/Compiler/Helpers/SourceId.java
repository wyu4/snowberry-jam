package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceId {
    EQUALS("=="),
    GREATER_THAN(">"),
    GREATER_OR_EQUAL_TO(">="),
    IF("IF"),
    IF_ELSE("IF_ELSE"),
    LESS_THAN("<"),
    LESS_OR_EQUAL_TO("<="),
    ON_RUN("ON_RUN"),
    PRINT("PRINT"),
    PROJECT("PROJECT"),
    SET("SET"),
    VARIABLE("VARIABLE");

    private final String id;
    SourceId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
