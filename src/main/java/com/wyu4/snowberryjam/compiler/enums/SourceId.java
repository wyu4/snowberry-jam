package com.wyu4.snowberryjam.compiler.enums;

/**
 * Enum of all the types of nodes that a Snowberry Jam source file should have
 */
public enum SourceId {
    AND("AND", "and"),
    ARRAY_OF("ARRAY_OF", "array of"),
    ELEMENT_AT_INDEX("ELEMENT_AT"),
    ERROR("ERROR", "error"),
    DECREASE_MACRO("-="),
    DIVIDE("/"),
    EQUALS("==", "="),
    GREATER_THAN(">"),
    GREATER_OR_EQUAL_TO(">=", "≥"),
    IF("IF", "if"),
    IF_ELSE("IF_ELSE", "if"),
    INCREASE_MACRO("+="),
    INPUT("INPUT", "input"),
    INPUT_MACRO("INPUT", "input"),
    LESS_THAN("<"),
    LESS_OR_EQUAL_TO("<=", "≤"),
    ON_RUN("ON_RUN"),
    OR("OR"),
    MINUS("-"),
    MODULUS("%"),
    MULTIPLY("*", "×"),
    NOT("NOT", "not "),
    NOT_EQUALS("!="),
    PARSE_NUMBER("PARSE_NUMBER", "parsed as number"),
    PLUS("+"),
    PRINT("PRINT", "print"),
    PROJECT("PROJECT"),
    PUBLIC_FOLDER("PUBLIC_FOLDER", "path to public folder"),
    RANDOM("RANDOM", "random"),
    READ_FILE("READ", "read"),
    REPEAT("REPEAT", "repeat"),
    ROUND("ROUND", "round"),
    SAME_TYPE("SAME_TYPE", "same type as"),
    SET("SET", "set variable"),
    SIZE_OF("SIZE_OF", "size of"),
    SPLIT("SPLIT", "split with"),
    THREAD("THREAD", "thread"),
    TIME("TIME", "time"),
    VARIABLE("VARIABLE", "variable"),
    WAIT("WAIT", "wait"),
    WAIT_UNTIL("WAIT_UNTIL", "wait until"),
    WARN("WARN", "warn"),
    WHILE("WHILE", "while"),
    WITH_UPDATED_ELEMENT("WITH_UPDATED_ELEMENT");

    /**
     * The string of what is actually found in {@code "id":...}
     */
    private final String id;

    /**
     * A more pretty version of the ID
     */
    private final String beautified;

    SourceId(String id) {
        this(id, null);
    }

    SourceId(String id, String beautified) {
        this.id = id;
        this.beautified = beautified;
    }

    /**
     * Get a pretty version of the ID
     * @return Value of variable {@link #beautified}. If null, returns the {@link #toString()}
     */
    public String getBeautified() {
        if (beautified == null) {
            return toString();
        }
        return beautified;
    }

    @Override
    public String toString() {
        return id;
    }
}
