package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Arithmetic Holder that handles multiplication between value A and value B.
 * @apiNote Supports boolean values, and will handle them algebraically.
 */
public class Multiply extends ArithmeticHolder {

    /**
     * Create a new modulus operation
     * @param node The {@link JsonNode} to refer
     */
    public Multiply(JsonNode node) {
        super(node);
    }

    /**
     * @return A * B for numerical values, while acting as an {@code AND} gate for boolean values.
     * @throws IllegalArgumentException Either A or B are not numerical or boolean
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            return ((double) getA().getValue()) * ((double) getB().getValue());
        } else if (typeA.equals(Boolean.class) && typeB.equals(Boolean.class)) {
            return ((boolean) getA().getValue()) && ((boolean) getB().getValue());
        }
        throw new IllegalArgumentException("Cannot perform multiplication on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s * %s".formatted(getA(), getB());
    }
}
