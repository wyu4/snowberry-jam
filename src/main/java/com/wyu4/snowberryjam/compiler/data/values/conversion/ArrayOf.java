package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Converts any value into an array.
 */
public class ArrayOf extends ConversionHolder {
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
     * @return {@link SourceId#ARRAY_OF}
     */
    @Override
    public SourceId getId() {
        return SourceId.ARRAY_OF;
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
        return "array of %s".formatted(getA());
    }
}
