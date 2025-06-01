package com.wyu4.snowberryjam.Compiler.DataType.Values.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;

import java.util.Arrays;

public class ElementAtIndex extends InteractiveValueHolder {
    public ElementAtIndex(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();

        if (!typeB.equals(Double.class)) {
            throw new IllegalArgumentException("Argument \"b\" %s (%s) is not a number.".formatted(getB().getString(), typeB.getCanonicalName()));
        }

        int index = (int) Math.round((double) getB().getValue());
        if (index < 0) {
            throw new IllegalArgumentException("Argument \"b\" %s must be greater or equal to 0.".formatted(index));
        }

        if (typeA.equals(Object[].class)) {
            Object[] array = (Object[]) getA().getValue();
            if (array.length < index) {
                throw new IllegalArgumentException("Array %s is too small for index %s".formatted(Arrays.toString(array), index));
            }
            return array[index];
        }

        String text = getA().getString();
        if (text.length() < index) {
            throw new IllegalArgumentException("String \"%s\" is too small for index %s".formatted(text, index));
        }
        return String.valueOf(text.charAt(index));
    }

    @Override
    public String toString() {
        return "index %s of %s".formatted(getB(), getA());
    }
}
