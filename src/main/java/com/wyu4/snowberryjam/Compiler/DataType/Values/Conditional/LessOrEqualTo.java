package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Conditional holder that handles an equality operation
 */
public class LessOrEqualTo extends ConditionalHolder {

    /**
     * Create a new Less-or-Equal-To operation
     * @param node The {@link JsonNode} to refer
     */
    public LessOrEqualTo(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if value A is less or equal to value B
     * @throws IllegalArgumentException if value A or B are empty.
     */
    @Override
    public Boolean getState() {
        if (!getA().notEmpty()) {
            throw new IllegalArgumentException("Value A is empty. Cannot perform Less-or-Equal-To operation.");
        } else if (!getB().notEmpty()) {
            throw new IllegalArgumentException("Value B is empty. Cannot perform Less-or-Equal-To operation.");
        }
        return getA().getSize() <= getB().getSize();
    }

    @Override
    public String toString() {
        return "%s is less or equal to %s".formatted(getA(), getB());
    }
}
