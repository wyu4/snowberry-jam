package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that passes A (radian) through the sine function.
 */
public class Sine extends ArithmeticHolder {

    /**
     * Create a new sine function
     * @param node The {@link JsonNode} to refer
     */
    public Sine(JsonNode node) {
        super(node);
    }

    /**
     * @return sin(A)
     * @throws IllegalArgumentException A isn't numerical
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (typeA.equals(Double.class)) {
            return Math.sin((double) getA().getValue());
        }
        throw new IllegalArgumentException("Cannot pass value with type [%s] through the sine function.".formatted(typeA.getCanonicalName()));
    }

    /**
     * @return {@link SourceId#SINE}
     */
    @Override
    public SourceId getId() {
        return SourceId.SINE;
    }

    @Override
    public String toString() {
        return "sin(%s)".formatted(getA());
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }
}
