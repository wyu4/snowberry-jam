package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Converts any value into it size.
 */
public class SizeOf extends ConversionHolder {
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
     * @return {@link SourceId#SIZE_OF}
     */
    @Override
    public SourceId getId() {
        return SourceId.SIZE_OF;
    }

    /**
     * @return Always return {@link Double}
     */
    @Override
    public Class<?> getType() {
        return Double.class;
    }

    @Override
    public String toString() {
        return "size of ".formatted(getA());
    }
}
