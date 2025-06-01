package com.wyu4.snowberryjam.Compiler.DataType.Values.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;

import java.util.Arrays;

public class ElementAtIndex extends ArrayHolder {
    public ElementAtIndex(JsonNode node) {
        super(node);
    }

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
