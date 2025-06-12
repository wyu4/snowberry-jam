package com.wyu4.snowberryjam.compiler.data.values.conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

import java.util.Arrays;

/**
 * Conditional holder that handles an inverted equality operation
 */
public class NotEquals extends ConditionalHolder {

    /**
     * Create a new inverted equality operation
     * @param node The {@link JsonNode} to refer
     */
    public NotEquals(JsonNode node) {
        super(node);
    }

    /**
     * @return {@code true} if values A and B are not equal
     */
    @Override
    public Boolean getState() {
        ValueHolder a = getA();
        ValueHolder b = getB();
        if (a.isType(Object[].class) && b.isType(Object[].class)) {
            return !Arrays.equals(a.getArray(), b.getArray());
        }
        return !a.pointsToSameValueAs(b);
    }

    /**
     * @return {@link SourceId#NOT_EQUALS}
     */
    @Override
    public SourceId getId() {
        return SourceId.NOT_EQUALS;
    }

    @Override
    public String toString() {
        return "%s doesn't equal %s".formatted(getA(), getB());
    }
}
