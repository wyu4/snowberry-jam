package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

import java.util.Arrays;

/**
 * Converts any value into an array by splitting its string form.
 */
public class SplitToArray extends InteractiveValueHolder {
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
     * @return Always return {@link Object} array.
     */
    @Override
    public Class<?> getType() {
        return Object[].class;
    }
}
