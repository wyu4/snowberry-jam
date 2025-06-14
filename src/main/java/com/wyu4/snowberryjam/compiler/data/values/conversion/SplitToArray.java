package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

import java.util.Arrays;

/**
 * Converts any value into an array by splitting its string form.
 */
public class SplitToArray extends ConversionHolder {
    /**
     * Create a new SplitToArray operation
     * @param node The {@link JsonNode} to refer
     */
    public SplitToArray(JsonNode node) {
        super(node);
    }

    /**
     * The array of a value.
     * @return An array casted to an {@link Object}
     * @see ValueHolder#getArray()
     */
    @Override
    public Object getValue() {
        if (getB().notEmpty()) {
            String[] split = getA().getString().split(getB().getString());
            return Arrays.copyOf(split, split.length, Object[].class);
        }
        return new Object[] {getA().getString()};
    }

    /**
     * @return {@link SourceId#SPLIT}
     */
    @Override
    public SourceId getId() {
        return SourceId.SPLIT;
    }

    /**
     * @return Always return {@link Object} array.
     */
    @Override
    public Class<?> getType() {
        return Object[].class;
    }

    @Override
    public String toString() {
        return "%s split to an array with pattern %s".formatted(getA(), getB());
    }
}
