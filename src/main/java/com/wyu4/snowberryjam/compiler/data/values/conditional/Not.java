package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Conditional Holder that inverts a value
 */
public class Not extends ConditionalHolder {

    /**
     * Create a new {@code NOT} inverter
     * @param node The {@link JsonNode} to refer
     */
    public Not(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code false} if value A is {@code true} (inverts value A). When value A is numerical, {@code false} if value A is greater than 0.
     * @throws IllegalArgumentException Value A has an unsupported type (not a {@link Boolean} or a {@link Double})
     */
    @Override
    public Object getValue() {
        Class<?> type = getA().getType();
        if (type.equals(Boolean.class)) {
            return !((boolean) getA().getValue());
        } else if (type.equals(Double.class)) {
            return !((double) getA().getValue() > 0);
        }

        throw new IllegalArgumentException("Cannot invert type [%s]".formatted(type.getCanonicalName()));
    }

    /**
     * @return {@link SourceId#NOT}
     */
    @Override
    public SourceId getId() {
        return SourceId.NOT;
    }

    @Override
    public String toString() {
        return "not %s".formatted(getA());
    }
}
