package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceKey {
    ID("id"),
    BODY("body"),
    VARIABLES("variables"),
    EVENTS("events"),
    NAME("name"),
    VALUE("value");

    private final String key;

    SourceKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
