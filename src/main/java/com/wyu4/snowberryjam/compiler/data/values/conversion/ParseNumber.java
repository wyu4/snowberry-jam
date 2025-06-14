package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Convert a string into a number
 */
public class ParseNumber extends ConversionHolder {

    /**
     * Create a new ParseNumber operation
     * @param node The {@link JsonNode} to refer
     */
    public ParseNumber(JsonNode node) {
        super(node);
    }

    /**
     * @return The {@link String} value, parsed as an {@link Double}
     */
    @Override
    public Object getValue() {
        String value = getA().getString();
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot convert to number: \"%s\" is not numerical.".formatted(value));
        }
    }

    /**
     * @return {@link SourceId#PARSE_NUMBER}
     */
    @Override
    public SourceId getId() {
        return SourceId.PARSE_NUMBER;
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
        return "%s parsed to a number".formatted(getA());
    }
}
