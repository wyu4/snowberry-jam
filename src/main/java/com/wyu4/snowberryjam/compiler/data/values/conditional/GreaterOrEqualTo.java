package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Conditional holder that handles an equality operation
 */
public class GreaterOrEqualTo extends ConditionalHolder {
    /**
     * Create a new Greater-or-Equal-To operation
     * @param node The {@link JsonNode} to refer
     */
    public GreaterOrEqualTo(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if value A is greater or equal to value B
     * @throws IllegalArgumentException if value A or B are empty.
     */
    @Override
    public Boolean getState() {
        if (!getA().notEmpty()) {
            throw new IllegalArgumentException("Value A is empty. Cannot perform Greater-or-Equal-To operation.");
        } else if (!getB().notEmpty()) {
            throw new IllegalArgumentException("Value B is empty. Cannot perform Greater-or-Equal-To operation.");
        }
        return getA().getSize() >= getB().getSize();
    }

    @Override
    public String toString() {
        return "%s is greater or equal to %s".formatted(getA(), getB());
    }
}
