package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

import java.util.Arrays;

/**
 * Conditional holder that handles an equality operation
 */
public class Equals extends ConditionalHolder {

    /**
     * Create a new equality operation
     * @param node The {@link JsonNode} to refer
     */
    public Equals(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if values A and B are equal
     */
    @Override
    public Boolean getState() {
        ValueHolder a = getA();
        ValueHolder b = getB();
        if (a.isType(Object[].class) && b.isType(Object[].class)) {
            return Arrays.equals(a.getArray(), b.getArray());
        }
        return a.equals(b);
    }

    @Override
    public String toString() {
        return "%s equal %s".formatted(getA(), getB());
    }
}
