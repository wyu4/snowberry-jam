package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class Minus extends ArithmeticHolder {

    public Minus(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            return ((double) getA().getValue()) - ((double) getB().getValue());
        }
        throw new ArithmeticException("Cannot perform subtraction on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }
}
