package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Arithmetic Holder that handles subtraction between value A and value B.
 */
public class Minus extends ArithmeticHolder {

    /**
     * Create a new subtraction operation
     * @param node The {@link JsonNode} to refer
     */
    public Minus(JsonNode node) {
        super(node);
    }

    /**
     * @return A - B
     * @throws IllegalArgumentException Either A or B are not numerical.
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            return getA().getSize() - getB().getSize();
        }
        throw new IllegalArgumentException("Cannot perform subtraction on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s - %s".formatted(getA(), getB());
    }
}
