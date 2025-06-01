package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceId {
    AND("AND"),
    ARRAY_OF("ARRAY_OF"),
    ELEMENT_AT_INDEX("ELEMENT_AT"),
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
    RANDOM("RANDOM"),
    ROUND("ROUND"),
    SAME_TYPE("SAME_TYPE"),
    SET("SET"),
    WITH_UPDATED_ELEMENT("WITH_UPDATED_ELEMENT"),
    SIZE_OF("SIZE_OF"),
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
