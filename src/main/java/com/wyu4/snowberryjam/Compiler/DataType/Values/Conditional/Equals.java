package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

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
        return getA().equals(getB());
    }

    @Override
    public String toString() {
        return "%s equal %s".formatted(getA(), getB());
    }
}
