package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that passes A (radian) through the arccosine (inverse cosine) function.
 */
public class ArcCosine extends ArithmeticHolder {

    /**
     * Create a new arccosine function
     * @param node The {@link JsonNode} to refer
     */
    public ArcCosine(JsonNode node) {
        super(node);
    }

    /**
     * @return arccos(A)
     * @throws IllegalArgumentException A isn't numerical, or is not within the range of -1.0 to 1.0
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (!typeA.equals(Double.class)) {
            throw new IllegalArgumentException("Cannot pass value with type [%s] through the arccosine function.".formatted(typeA.getCanonicalName()));
        }

        final double a = (double) getA().getValue();
        if (Math.abs(a) > 1 ) {
            throw new IllegalArgumentException("Value [%s] is not within range of -1.0 to 1.0.".formatted(a));
        }
        return Math.acos(a);
    }

    /**
     * @return {@link SourceId#ARCCOSINE}
     */
    @Override
    public SourceId getId() {
        return SourceId.ARCCOSINE;
    }

    @Override
    public String toString() {
        return "arccos(%s)".formatted(getA());
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }
}
