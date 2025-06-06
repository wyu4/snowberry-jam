package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Arithmetic Holder that rounds value A.
 */
public class Round extends ArithmeticHolder {

    /**
     * Create a new rounding operation
     * @param node The {@link JsonNode} to refer
     */
    public Round(JsonNode node) {
        super(node);
    }

    /**
     * @return Value A rounded to the nearest unity
     * @throws IllegalArgumentException A is not numeric
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (typeA.equals(Double.class)) {
            return (double) Math.round((double) getA().getValue());
        }
        throw new IllegalArgumentException("Cannot round value with type [%s].".formatted(typeA.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "%s rounded".formatted(getA());
    }
}
