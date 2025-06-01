package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

public class Plus extends ArithmeticHolder {

    public Plus(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(String.class) || typeB.equals(String.class)) {
            return getA().getString().concat(getB().getString());
        } else if (typeA.equals(Boolean.class) && typeB.equals(Boolean.class)) {
            return ((boolean) getA().getValue()) || ((boolean) getB().getValue());
        }
        return getA().getSize() + getB().getSize();
    }

    @Override
    public String toString() {
        return "%s + %s".formatted(getA(), getB());
    }
}
