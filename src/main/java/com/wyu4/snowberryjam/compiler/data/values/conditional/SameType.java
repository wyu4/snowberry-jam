package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

/**
 * Conditional Holder that checks if two values share the same type.
 */
public class SameType extends ConditionalHolder {

    /**
     * Create a new Same-Type operation
     * @param node The {@link JsonNode} to refer
     */
    public SameType(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if value A and value B share the same type.
     * @see ValueHolder#getType()
     */
    @Override
    public Boolean getState() {
        return getA().isType(getB().getType());
    }

    @Override
    public String toString() {
        return "%s and %s share the same type".formatted(getA(), getB());
    }
}
