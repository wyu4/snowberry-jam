package com.wyu4.snowberryjam.Compiler.DataType;

public enum CoreId {
    PROJECT("PROJECT"),
    VARIABLE("VARIABLE");

    final String name;
    CoreId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
