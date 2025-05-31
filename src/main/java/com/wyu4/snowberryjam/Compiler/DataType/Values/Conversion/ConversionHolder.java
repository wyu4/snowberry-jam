package com.wyu4.snowberryjam.Compiler.DataType.Values.Conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class ConversionHolder extends ValueHolder {
    private final ValueHolder a;

    public ConversionHolder(JsonNode node) {
        this(fromNode(node.get(SourceKey.PARAM_A.toString())));
    }

    public ConversionHolder(ValueHolder a) {
        this.a = a;
    }

    protected ValueHolder getA() {
        return a;
    }
}
