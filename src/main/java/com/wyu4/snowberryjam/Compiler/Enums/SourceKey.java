package com.wyu4.snowberryjam.Compiler.Enums;

/**
 * The keys that can be found in a Snowberry Jam source code.
 */
public enum SourceKey {
    ARRAY("array"),
    BODY("body"),
    ELSE("else"),
    EVENTS("events"),
    ID("id"),
    NAME("name"),
    PARAM_A("a"),
    PARAM_B("b"),
    PARAM_I("i"),
    VARIABLES("variables"),
    VALUE("value"),;

    /**
     * The string format of the enum
     */
    private final String key;

    SourceKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
