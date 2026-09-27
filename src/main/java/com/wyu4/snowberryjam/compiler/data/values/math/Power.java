package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that handles power exponents (A^B)
 */
public class Power extends ArithmeticHolder {

    /**
     * Create a new power operation
     * @param node The {@link JsonNode} to refer
     */
    public Power(JsonNode node) {
        super(node);
    }

    public Power(ValueHolder a, ValueHolder b) {
        super(a, b);
    }

    /**
     * @return A^B
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Double.class) || typeB.equals(Double.class)) {
            return Math.pow((double) getA().getValue(), (double) getB().getValue());
        }
        throw new IllegalArgumentException("Cannot perform [%s]^[%s].".formatted( typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    /**
     * @return {@link SourceId#POWER}
     */
    @Override
    public SourceId getId() {
        return SourceId.POWER;
    }

    @Override
    public String toString() {
        return "(%s)^(%s)".formatted(getA(), getB());
    }
}
