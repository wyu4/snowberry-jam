package com.wyu4.snowberryjam.compiler.data.values.iteration;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

import java.util.Arrays;

/**
 * Array Holder that returns an array with a modified element
 */
public class WithUpdatedElement extends ArrayHolder {

    /**
     * Create a new With-Updated-Element
     * @param node The {@link JsonNode} to refer
     */
    public WithUpdatedElement(JsonNode node) {
        super(node);
    }

    /**
     * @return The array of value A with the element at value I swapped out for value B
     * @throws ArrayIndexOutOfBoundsException Value I is greater than the array size of value A
     */
    @Override
    public Object getValue() {
        Object[] array = getA().getArray();
        int index = getValidIndex();
        if (array.length < index) {
            throw new ArrayIndexOutOfBoundsException("Array %s is too small for index %s".formatted(Arrays.toString(array), index));
        }
        array[index] = getB().getValue();
        return array;
    }

    /**
     * @return {@link SourceId#WITH_UPDATED_ELEMENT}
     */
    @Override
    public SourceId getId() {
        return SourceId.WITH_UPDATED_ELEMENT;
    }

    @Override
    public Class<?> getType() {
        return Object[].class;
    }

    @Override
    public String toString() {
        return "index %s of %s swapped with %s".formatted(getI(), getA(), getB());
    }
}
