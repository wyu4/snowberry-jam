package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Arithmetic Holder that handles modulus operations between value A and value B.
 */
public class Modulus extends ArithmeticHolder {

    /**
     * Create a new modulus operation
     * @param node The {@link JsonNode} to refer
     */
    public Modulus(JsonNode node) {
        super(node);
    }

    /**
     * @return A % B
     * @throws ArithmeticException B is 0
     * @throws IllegalArgumentException Either A or B are not numerical
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) && typeB.equals(Double.class)) {
            double valueB = ((double) getB().getValue());
            if (valueB == 0) {
                throw new ArithmeticException("Cannot modulus %s by 0.".formatted(getA().getString()));
            }
            return ((double) getA().getValue()) % valueB;
        }
        throw new IllegalArgumentException("Cannot perform division on values type [%s] and [%s].".formatted(typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s %% %s".formatted(getA(), getB());
    }
}
