package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

public class SameType extends ConditionalHolder {

    public SameType(JsonNode node) {
        super(node);
    }

    @Override
    public Boolean getState() {
        return getA().getType().equals(getB().getType());
    }

    @Override
    public String toString() {
        return "%s and %s share the same type".formatted(getA(), getB());
    }
}
