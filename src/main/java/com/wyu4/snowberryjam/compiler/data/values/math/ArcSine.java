package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that passes A (radian) through the arcsine (inverse sine) function.
 */
public class ArcSine extends ArithmeticHolder {

    /**
     * Create a new arcsine function
     * @param node The {@link JsonNode} to refer
     */
    public ArcSine(JsonNode node) {
        super(node);
    }

    /**
     * @return arcsin(A)
     * @throws IllegalArgumentException A isn't numerical, or is not within the range of -1.0 to 1.0
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        if (!typeA.equals(Double.class)) {
            throw new IllegalArgumentException("Cannot pass value with type [%s] through the arcsine function.".formatted(typeA.getCanonicalName()));
        }

        final double a = (double) getA().getValue();
        if (Math.abs(a) > 1 ) {
            throw new IllegalArgumentException("Value [%s] is not within range of -1.0 to 1.0.".formatted(a));
        }
        return Math.asin(a);
    }

    /**
     * @return {@link SourceId#ARCSINE}
     */
    @Override
    public SourceId getId() {
        return SourceId.ARCSINE;
    }

    @Override
    public String toString() {
        return "arcsin(%s)".formatted(getA());
    }

    @Override
    public Class<?> getType() {
        return Double.TYPE;
    }
}
