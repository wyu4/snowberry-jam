package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceId {
    EQUALS("EQUALS"),
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
