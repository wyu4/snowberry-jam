package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

/**
 * Arithmetic Holder that handles addition between value A and value B.
 * @apiNote Can also be used for string concatenation and array appending/merging, as well as boolean algebra gate for boolean values.
 */
public class Plus extends ArithmeticHolder {

    /**
     * Create a new addition operation
     * @param node The {@link JsonNode} to refer
     */
    public Plus(JsonNode node) {
        super(node);
    }

    /**
     * @return A + B for numerical values. Concatenates strings, acts as an {@code OR} gate for boolean values, and appends or merges values to arrays.
     */
    @Override
    public Object getValue() {
        Class<?> typeA = getA().getType();
        Class<?> typeB = getB().getType();
        if (typeA.equals(Object[].class)) {
            Object[] a = getA().getArray();
            Object[] mergedArray;
            if (typeB.equals(Object[].class)) {
                Object[] b = getB().getArray();
                mergedArray = Arrays.copyOf(a, a.length + b.length);
                System.arraycopy(b, 0, mergedArray, a.length, b.length);
            } else {
                mergedArray = new Object[a.length + 1];
                System.arraycopy(a, 0, mergedArray, 0, a.length);
                mergedArray[a.length] = getB().getValue();
            }
            return mergedArray;
        }
        if (typeA.equals(String.class) || typeB.equals(String.class)) {
            return getA().getString().concat(getB().getString());
        } else if (typeA.equals(Boolean.class) && typeB.equals(Boolean.class)) {
            return ((boolean) getA().getValue()) || ((boolean) getB().getValue());
        }
        return getA().getSize() + getB().getSize();
    }

    @Override
    public String toString() {
        return "%s + %s".formatted(getA(), getB());
    }
}
