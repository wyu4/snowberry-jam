package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Conditional holder that handles an equality operation
 */
public class GreaterThan extends ConditionalHolder {

    /**
     * Create a new Greater-Than operation
     * @param node The {@link JsonNode} to refer
     */
    public GreaterThan(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if value A is greater than value B
     * @throws IllegalArgumentException if value A or B are empty.
     */
    @Override
    public Boolean getState() {
        if (!getA().notEmpty()) {
            throw new IllegalArgumentException("Value A is empty. Cannot perform Greater-Than operation.");
        } else if (!getB().notEmpty()) {
            throw new IllegalArgumentException("Value B is empty. Cannot perform Greater-Than operation.");
        }
        return getA().getSize() > getB().getSize();
    }

    /**
     * @return {@link SourceId#GREATER_THAN}
     */
    @Override
    public SourceId getId() {
        return SourceId.GREATER_THAN;
    }

    @Override
    public String toString() {
        return "%s is greater than %s".formatted(getA(), getB());
    }
}
