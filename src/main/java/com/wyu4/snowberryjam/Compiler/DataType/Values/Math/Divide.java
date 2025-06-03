package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Arithmetic Holder that handles division between value A and value B.
 */
public class Divide extends ArithmeticHolder {

    /**
     * Create a new division operation
     * @param node The {@link JsonNode} to refer
     */
    public Divide(JsonNode node) {
        super(node);
    }

    /**
     * @return A / B
     * @throws ArithmeticException B is 0
     * @throws IllegalArgumentException Either A or B aren't numerical
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            double valueB = getB().getSize();
            if (valueB == 0) {
                throw new ArithmeticException("Cannot divide %s by 0.".formatted(getA().getString()));
            }
            return ((double) getA().getValue()) / valueB;
        }
        throw new IllegalArgumentException("Cannot perform division on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s / %s".formatted(getA(), getB());
    }
}
