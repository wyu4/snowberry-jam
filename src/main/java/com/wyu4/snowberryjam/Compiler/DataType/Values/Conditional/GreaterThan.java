package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

public class GreaterThan extends ConditionalHolder {

    public GreaterThan(JsonNode node) {
        super(node);
    }

    @Override
    public Boolean getState() {
        Double sizeA = getA().getSize();
        Double sizeB = getB().getSize();
        if (sizeA == null || sizeB == null) {
            return false;
        }
        return sizeA > sizeB;
    }

    @Override
    public String toString() {
        return "%s is greater than %s".formatted(getA(), getB());
    }
}
