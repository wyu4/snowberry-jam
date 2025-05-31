package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

public class CoreElement {
    private final SourceId id;
    private final ValueHolder name;

    public CoreElement(SourceId id) {
        this(id, new ValueHolder(null));
    }

    public CoreElement(SourceId id, ValueHolder name) {
        this.id = id;
        this.name = name;
    }

    public SourceId getId() {
        return id;
    }

    public String getName() {
        return name.getString();
    }

    public boolean isValid() {
        return true;
    };
}
