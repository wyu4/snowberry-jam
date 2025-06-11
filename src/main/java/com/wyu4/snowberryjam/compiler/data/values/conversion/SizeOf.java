package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

/**
 * Converts any value into it size.
 */
public class SizeOf extends InteractiveValueHolder {
    /**
     * Create a new SizeOf operation
     * @param node The {@link JsonNode} to refer
     */
    public SizeOf(JsonNode node) {
        super(node);
    }

    /**
     * The size of a value.
     * @return A {@link Double} casted to an {@link Object}
     * @see ValueHolder#getSize()
     */
    @Override
    public Object getValue() {
        return getA().getSize();
    }

    /**
     * @return Always return {@link Double}
     */
    @Override
    public Class<?> getType() {
        return Double.class;
    }
}
