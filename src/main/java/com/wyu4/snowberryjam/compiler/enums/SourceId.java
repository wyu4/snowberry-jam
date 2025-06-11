package com.wyu4.snowberryjam.compiler.enums;

/**
 * Enum of all the types of nodes that a Snowberry Jam source file should have
 */
public enum SourceId {
    AND("AND"),
    ARRAY_OF("ARRAY_OF"),
    ELEMENT_AT_INDEX("ELEMENT_AT"),
    ERROR("ERROR"),
    DECREASE_MACRO("-="),
    DIVIDE("/"),
    EQUALS("=="),
    GREATER_THAN(">"),
    GREATER_OR_EQUAL_TO(">="),
    IF("IF"),
    IF_ELSE("IF_ELSE"),
    INCREASE_MACRO("+="),
    INPUT("INPUT"),
    INPUT_MACRO("INPUT"),
    LESS_THAN("<"),
    LESS_OR_EQUAL_TO("<="),
    ON_RUN("ON_RUN"),
    OR("OR"),
    MINUS("-"),
    MODULUS("%"),
    MULTIPLY("*"),
    NOT("NOT"),
    NOT_EQUALS("!="),
    PLUS("+"),
    PRINT("PRINT"),
    PROJECT("PROJECT"),
    PUBLIC_FOLDER("PUBLIC_FOLDER"),
    RANDOM("RANDOM"),
    READ_FILE("READ"),
    REPEAT("REPEAT"),
    ROUND("ROUND"),
    SAME_TYPE("SAME_TYPE"),
    SET("SET"),
    SIZE_OF("SIZE_OF"),
    THREAD("THREAD"),
    TIME("TIME"),
    VARIABLE("VARIABLE"),
    WAIT("WAIT"),
    WAIT_UNTIL("WAIT_UNTIL"),
    WARN("WARN"),
    WHILE("WHILE"),
    WITH_UPDATED_ELEMENT("WITH_UPDATED_ELEMENT");

    /**
     * The string of what is actually found in {@code "id":...}
     */
    private final String id;
    SourceId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
