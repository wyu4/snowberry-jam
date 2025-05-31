package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class InteractiveValueHolder extends ValueHolder {
    private final ValueHolder a;
    private final ValueHolder b;

    public InteractiveValueHolder(JsonNode node) {
        this(
                fromNode(node.get(SourceKey.PARAM_A.toString())),
                fromNode(node.get(SourceKey.PARAM_B.toString()))
        );
    }

    public InteractiveValueHolder(ValueHolder a, ValueHolder b) {
        this.a = a;
        this.b = b;
    }

    protected ValueHolder getA() {
        return a;
    }

    protected ValueHolder getB() {
        return b;
    }
}
