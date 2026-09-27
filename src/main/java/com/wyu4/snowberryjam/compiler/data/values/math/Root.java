package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Arithmetic Holder that handles root(A) with optional base B (default base of 2)
 */
public class Root extends ArithmeticHolder {

    /**
     * Create a new root operation
     * @param node The {@link JsonNode} to refer
     */
    public Root(JsonNode node) {
        super(node);
    }

    /**
     * @return root(A) with base B. If B is not defined, a squareroot will be performed.
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        
        final double base;
        if (typeB.equals(Double.class)) {
            base = (double) getB().getValue();
        } else {
            base = 2;
        }
        if (typeA.equals(Double.class)) {
            return Math.pow((double) getA().getValue(), 1.0 / base);
        }
       
        throw new IllegalArgumentException("Cannot perform [%s]_root[%s].".formatted( typeA.getCanonicalName(), typeB.getCanonicalName()));
    }

    /**
     * @return {@link SourceId#ROOT}
     */
    @Override
    public SourceId getId() {
        return SourceId.ROOT;
    }

    @Override
    public String toString() {
        final ValueHolder b = getB();
        return "(%s)_root^(%s)".formatted(b.notEmpty() ? b : 2, getA());
    }
}
