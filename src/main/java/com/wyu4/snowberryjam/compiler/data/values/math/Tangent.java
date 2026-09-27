package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that passes A (radian) through the tangent function.
 */
public class Tangent extends ArithmeticHolder {

    /**
     * Create a new tangent function
     * @param node The {@link JsonNode} to refer
     */
    public Tangent(JsonNode node) {
        super(node);
    }

    /**
     * @return tan(A)
     * @throws IllegalArgumentException A isn't numerical
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (typeA.equals(Double.class)) {
            return Math.tan((double) getA().getValue());
        }
        throw new IllegalArgumentException("Cannot pass value with type [%s] through the tangent function.".formatted(typeA.getCanonicalName()));
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
        return "tan(%s)".formatted(getA());
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }
}
