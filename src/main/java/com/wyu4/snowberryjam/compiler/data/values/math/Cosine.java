package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that passes A (radian) through the cosine function.
 */
public class Cosine extends ArithmeticHolder {

    /**
     * Create a new cosine function
     * @param node The {@link JsonNode} to refer
     */
    public Cosine(JsonNode node) {
        super(node);
    }

    /**
     * @return cos(A)
     * @throws IllegalArgumentException A isn't numerical
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (typeA.equals(Double.class)) {
            return Math.cos((double) getA().getValue());
        }
        throw new IllegalArgumentException("Cannot pass value with type [%s] through the cosine function.".formatted(typeA.getCanonicalName()));
    }

    /**
     * @return {@link SourceId#TANGENT}
     */
    @Override
    public SourceId getId() {
        return SourceId.TANGENT;
    }

    @Override
    public String toString() {
        return "cos(%s)".formatted(getA());
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }
}
