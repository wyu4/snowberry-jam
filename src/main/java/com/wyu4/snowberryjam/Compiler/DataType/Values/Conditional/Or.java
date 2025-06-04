package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Conditional Holder that handles {@code OR} conditions
 */
public class Or extends ConditionalHolder {

    /**
     * Create a new {@code OR} operation
     * @param node The {@link JsonNode} to refer
     */
    public Or(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if values A or B are {@code true}. If values are numerical, {@code true} if one of both values are greater than 0. If values are {@link String}, {@code true} if one of both strings are not empty. Otherwise, {@code true} if one of both pointers are not empty.
     * @see #bothTypesAre(Class)
     */
    @Override
    public Boolean getState() {
        if (bothTypesAre(Boolean.class)) {
            return ((boolean) getA().getValue()) || ((boolean) getB().getValue());
        } else if (bothTypesAre(Double.class)) {
            return ((double) getA().getValue() > 0D) || ((double) getB().getValue() > 0D);
        } else if (bothTypesAre(String.class)) {
            return !((String) getA().getValue()).isEmpty() || !((String) getB().getValue()).isEmpty();
        }
        return getA().notEmpty() || getB().notEmpty();
    }

    @Override
    public String toString() {
        return "%s or %s are true".formatted(getA(), getB());
    }
}
