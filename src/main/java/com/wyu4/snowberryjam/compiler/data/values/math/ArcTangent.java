package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that passes A (radian) through the arctangent (inverse tangent) function.
 */
public class ArcTangent extends ArithmeticHolder {

    /**
     * Create a new arccosine function
     * @param node The {@link JsonNode} to refer
     */
    public ArcTangent(JsonNode node) {
        super(node);
    }

    /**
     * @return arctan(A)
     * @throws IllegalArgumentException A isn't numerical
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (!typeA.equals(Double.class)) {
            throw new IllegalArgumentException("Cannot pass value with type [%s] through the arctangent function.".formatted(typeA.getCanonicalName()));
        }

        return Math.atan((double) getA().getValue());
    }

    /**
     * @return {@link SourceId#ARCTANGENT}
     */
    @Override
    public SourceId getId() {
        return SourceId.ARCTANGENT;
    }

    @Override
    public String toString() {
        return "arctan(%s)".formatted(getA());
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }
}
