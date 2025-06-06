package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Conditional holder that handles an equality operation
 */
public class LessThan extends ConditionalHolder {

    /**
     * Create a new Less-Than operation
     * @param node The {@link JsonNode} to refer
     */
    public LessThan(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if value A is less than value B
     * @throws IllegalArgumentException if value A or B are empty.
     */
    @Override
    public Boolean getState() {
        if (!getA().notEmpty()) {
            throw new IllegalArgumentException("Value A is empty. Cannot perform Less-Than operation.");
        } else if (!getB().notEmpty()) {
            throw new IllegalArgumentException("Value B is empty. Cannot perform Less-Than operation.");
        }
        return getA().getSize() < getB().getSize();
    }

    @Override
    public String toString() {
        return "%s is less than %s".formatted(getA(), getB());
    }
}
