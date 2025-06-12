package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Conditional Holder that handles {@code AND} operations.
 */
public class And extends ConditionalHolder {

    /**
     * Create a new {@code AND} operation
     * @param node The {@link JsonNode} to refer
     */
    public And(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if values A and B are {@code true}. If values are numerical, {@code true} if both values are greater than 0. If values are {@link String}, {@code true} if both strings are not empty. Otherwise, {@code true} if both pointers are not empty.
     * @see #bothTypesAre(Class)
     */
    @Override
    public Boolean getState() {
        if (bothTypesAre(Boolean.class)) {
            return ((boolean) getA().getValue()) && ((boolean) getB().getValue());
        } else if (bothTypesAre(Double.class)) {
            return ((double) getA().getValue() > 0D) && ((double) getB().getValue() > 0D);
        } else if (bothTypesAre(String.class)) {
            return !((String) getA().getValue()).isEmpty() && !((String) getB().getValue()).isEmpty();
        }
        return getA().notEmpty() && getB().notEmpty();
    }

    /**
     * @return {@link SourceId#AND}
     */
    @Override
    public SourceId getId() {
        return SourceId.AND;
    }

    @Override
    public String toString() {
        return "%s and %s are true".formatted(getA(), getB());
    }
}
