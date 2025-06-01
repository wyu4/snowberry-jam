package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceId {
    AND("AND"),
    DIVIDE("/"),
    EQUALS("=="),
    GREATER_THAN(">"),
    GREATER_OR_EQUAL_TO(">="),
    IF("IF"),
    IF_ELSE("IF_ELSE"),
    LESS_THAN("<"),
    LESS_OR_EQUAL_TO("<="),
    ON_RUN("ON_RUN"),
    OR("OR"),
    MINUS("-"),
    MODULUS("%"),
    MULTIPLY("*"),
    NOT("NOT"),
    PLUS("+"),
    PRINT("PRINT"),
    PROJECT("PROJECT"),
    RANDOM("random"),
    SET("SET"),
    TIME("TIME"),
    VARIABLE("VARIABLE"),
    WHILE("WHILE");

    private final String id;
    SourceId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
