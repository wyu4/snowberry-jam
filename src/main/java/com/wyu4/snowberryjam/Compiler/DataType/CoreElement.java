package com.wyu4.snowberryjam.Compiler.DataType;

public abstract class CoreElement {
    private final CoreId id;
    private final String name;

    public CoreElement(CoreId id) {
        this(id, null);
    }

    public CoreElement(CoreId id, String name) {
        this.id = id;
        this.name = name;
    }

    public CoreId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    abstract boolean isValid();
}
