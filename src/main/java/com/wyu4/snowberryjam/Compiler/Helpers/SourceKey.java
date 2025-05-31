package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceKey {
    BODY("body"),
    ELSE("else"),
    EVENTS("events"),
    ID("id"),
    NAME("name"),
    PARAM_A("a"),
    PARAM_B("b"),
    VARIABLES("variables"),
    VALUE("value"),;

    private final String key;

    SourceKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
