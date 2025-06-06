package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

/**
 * Converts any value into an array.
 */
public class ArrayOf extends InteractiveValueHolder {
    /**
     * Create a new ArrayOf operation
     * @param node The {@link JsonNode} to refer
     */
    public ArrayOf(JsonNode node) {
        super(node);
    }

    /**
     * The array of a value.
     * @return An array casted to an {@link Object}
     * @see ValueHolder#getArray()
     */
    @Override
    public Object getValue() {
        return getA().getArray();
    }

    /**
     * @return Always return {@link Object} array.
     */
    @Override
    public Class<?> getType() {
        return Object[].class;
    }
}
