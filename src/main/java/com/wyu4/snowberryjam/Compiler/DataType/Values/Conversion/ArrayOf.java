package com.wyu4.snowberryjam.Compiler.DataType.Values.Conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;

public class ArrayOf extends InteractiveValueHolder {
    public ArrayOf(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        return getA().getArray();
    }

    @Override
    public Class<?> getType() {
        return Object[].class;
    }
}
