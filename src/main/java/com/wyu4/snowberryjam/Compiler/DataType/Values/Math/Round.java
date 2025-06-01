package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

public class Round extends ArithmeticHolder {

    public Round(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (typeA.equals(Double.class)) {
            return (double) Math.round((double) getA().getValue());
        }
        throw new ArithmeticException("Cannot round value with type [%s].".formatted(typeA.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s rounded".formatted(getA());
    }
}
