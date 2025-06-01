package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

public class Divide extends ArithmeticHolder {

    public Divide(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            double valueB = ((double) getB().getValue());
            if (valueB == 0) {
                throw new ArithmeticException("Cannot divide %s by 0.".formatted(getA().getString()));
            }
            return ((double) getA().getValue()) / valueB;
        }
        throw new ArithmeticException("Cannot perform division on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s / %s".formatted(getA(), getB());
    }
}
