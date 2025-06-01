package com.wyu4.snowberryjam.Compiler.DataType.Values.Array;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

public class WithUpdatedElement extends ArrayHolder {
    public WithUpdatedElement(JsonNode node) {
        super(node);
    }

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

    @Override
    public Class<?> getType() {
        return Object[].class;
    }
}
