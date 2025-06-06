package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;

/**
 * A default unset conditional holder. This handles conditional operations.
 */
public class ConditionalHolder extends InteractiveValueHolder {

    /**
     * Create a new Conditional Holder
     * @param node The {@link JsonNode} to refer
     */
    public ConditionalHolder(JsonNode node) {
        super(node);
    }

    /**
     * Get the state of the condition
     * @return {@link Boolean} value. Differs depending on each type of condition.
     */
    public Boolean getState() {
        return true;
    }

    /**
     * Check if both types of value A and B are a certain type.
     * @param type Type to check for
     * @return {@code true} or {@code false}
     */
    public boolean bothTypesAre(Class<?> type) {
        return getA().isType(type) && getB().isType(type);
    }

    /**
     * @return A {@link Boolean}, casted to an {@link Object}.
     * @see #getState()
     */
    @Override
    public Object getValue() {
        return getState();
    }

    /**
     * @return Always returns {@link Boolean}
     */
    @Override
    public Class<?> getType() {
        return Boolean.class;
    }

    @Override
    public boolean notEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "default unset conditional holder with parameters %s and %s".formatted(getA(), getB());
    }
}
