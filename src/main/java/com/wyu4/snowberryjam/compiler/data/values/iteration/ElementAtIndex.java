package com.wyu4.snowberryjam.compiler.data.values.iteration;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

/**
 * Array Holder that handles element search given the index
 */
public class ElementAtIndex extends ArrayHolder {

    /**
     * Create a new Element-At-Index
     * @param node The {@link JsonNode} to refer
     */
    public ElementAtIndex(JsonNode node) {
        super(node);
    }

    /**
     * @return The element at index I in the array of value A
     * @throws ArrayIndexOutOfBoundsException Value I is greater than the array size of value A
     */
    @Override
    public Object getValue() {
        Object[] array = getA().getArray();
        int index = getValidIndex();
        if (array.length < index) {
            throw new ArrayIndexOutOfBoundsException("Array %s is too small for index %s".formatted(Arrays.toString(array), index));
        }
        return array[index];
    }

    @Override
    public String toString() {
        return "index %s of %s".formatted(getI(), getA());
    }
}
