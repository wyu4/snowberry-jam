package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

public abstract class CoreElement {
    private final SourceId id;
    private final String name;

    public CoreElement(SourceId id) {
        this(id, null);
    }

    public CoreElement(SourceId id, String name) {
        this.id = id;
        this.name = name;
    }

    public SourceId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    abstract boolean isValid();
}
