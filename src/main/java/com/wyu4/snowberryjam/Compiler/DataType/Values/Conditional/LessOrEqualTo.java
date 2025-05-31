package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

public class LessOrEqualTo extends ConditionalHolder {
    public LessOrEqualTo(JsonNode node) {
        super(node);
    }

    @Override
    public Boolean getState() {
        Double sizeA = getA().getSize();
        Double sizeB = getB().getSize();
        if (sizeA == null || sizeB == null) {
            return false;
        }
        return sizeA <= sizeB;
    }

    @Override
    public String toString() {
        return "%s is less or equal to %s".formatted(getA(), getB());
    }
}
