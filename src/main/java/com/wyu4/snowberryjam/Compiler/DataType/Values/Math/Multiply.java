package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

public class Multiply extends ArithmeticHolder {

    public Multiply(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            return ((double) getA().getValue()) * ((double) getB().getValue());
        } else if (typeA.equals(Boolean.class) && typeB.equals(Boolean.class)) {
            return ((boolean) getA().getValue()) && ((boolean) getB().getValue());
        }
        throw new ArithmeticException("Cannot perform multiplication on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s * %s".formatted(getA(), getB());
    }
}
