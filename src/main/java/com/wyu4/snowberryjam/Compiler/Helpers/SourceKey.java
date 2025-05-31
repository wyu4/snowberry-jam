package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceKey {
    ID("id"),
    BODY("body"),
    VARIABLES("variables"),
    EVENTS("events"),
    NAME("name"),
    VALUE("value"),

    PARAM_A("a"),
    PARAM_B("b");

    private final String key;

    SourceKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
