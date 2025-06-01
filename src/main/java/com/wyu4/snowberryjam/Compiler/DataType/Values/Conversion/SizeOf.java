package com.wyu4.snowberryjam.Compiler.DataType.Values.Conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;

public class SizeOf extends InteractiveValueHolder {
    public SizeOf(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        return getA().getSize();
    }

    @Override
    public Class<?> getType() {
        return Double.class;
    }
}
