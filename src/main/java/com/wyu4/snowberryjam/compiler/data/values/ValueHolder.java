package com.wyu4.snowberryjam.compiler.data.values;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.data.values.iteration.ElementAtIndex;
import com.wyu4.snowberryjam.compiler.data.values.iteration.WithUpdatedElement;
import com.wyu4.snowberryjam.compiler.data.values.builtin.RandomHolder;
import com.wyu4.snowberryjam.compiler.data.values.builtin.TimeHolder;
import com.wyu4.snowberryjam.compiler.data.values.conditional.*;
import com.wyu4.snowberryjam.compiler.data.values.conversion.ArrayOf;
import com.wyu4.snowberryjam.compiler.data.values.conversion.SizeOf;
import com.wyu4.snowberryjam.compiler.data.values.math.*;
import com.wyu4.snowberryjam.compiler.enums.EnumHelper;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

import javax.lang.model.type.NullType;
import java.util.Arrays;

/**
 * A pointer to a stored value.
 */
public class ValueHolder {

    /**
     * Check if a {@link JsonNode} is primitive, in the context of Snowberry Jam
     * @param node Node
     * @return {@code true} if the value is a {@link String}, {@link Boolean}, {@link Double}, or {@code Array}. Returns {@code false} otherwise.
     * @see JsonNode
     */
    private static boolean isPrimitive(JsonNode node) {
        return node.isTextual() || node.isBoolean() || node.isNumber() || node.isArray();
    }

    /**
     * Manufacture a ValueHolder object from a {@link JsonNode} object. Uses the ID node to identify which type of {@link ValueHolder} should be created
     * @param node Node containing Snowberry Jam Source code
     * @return A ValueHolder object. Might be casted from child classes.
     * @see com.wyu4.snowberryjam.compiler.data.values
     */
    public static ValueHolder fromNode(JsonNode node) {
        if (node == null) {
            return new ValueHolder();
        }

        if (isPrimitive(node)) {
            return new ValueHolder(Compiler.asPrimitiveObject(node));
        }

        SourceId id = EnumHelper.stringToId(Compiler.getId(node));
        if (id == null) {
            return new ValueHolder();
        }

        return switch (id) {
            case VARIABLE -> new VariableReference(Compiler.getName(node));
            case EQUALS -> new Equals(node);
            case GREATER_THAN -> new GreaterThan(node);
            case GREATER_OR_EQUAL_TO -> new GreaterOrEqualTo(node);
            case LESS_THAN -> new LessThan(node);
            case LESS_OR_EQUAL_TO -> new LessOrEqualTo(node);
            case SAME_TYPE -> new SameType(node);
            case AND -> new And(node);
            case OR -> new Or(node);
            case NOT -> new Not(node);
            case PLUS -> new Plus(node);
            case MINUS -> new Minus(node);
            case MULTIPLY -> new Multiply(node);
            case DIVIDE -> new Divide(node);
            case MODULUS -> new Modulus(node);
            case ROUND -> new Round(node);
            case SIZE_OF -> new SizeOf(node);
            case ARRAY_OF -> new ArrayOf(node);
            case ELEMENT_AT_INDEX -> new ElementAtIndex(node);
            case WITH_UPDATED_ELEMENT -> new WithUpdatedElement(node);
            case RANDOM -> new RandomHolder();
            case TIME -> new TimeHolder();
            default -> throw new IllegalArgumentException("Non-primitive node with ID \"%s\" is not a registered value type.".formatted(id));
        };
    }

    /**
     * Check if a {@link ValueHolder} object is conditional
     * @param condition {@link ValueHolder} object to check
     * @return The {@code condition} parameter, untouched.
     * @throws IllegalArgumentException if the value holder is not conditional.
     */
    public static ValueHolder checkValueIsConditional(ValueHolder condition) throws IllegalArgumentException {
        if (!condition.isType(Boolean.class)) {
            throw new IllegalArgumentException("Value passed as condition is not conditional.");
        }
        return condition;
    }

    /**
     * The value to be pointing to. Could be another {@link ValueHolder} object, or null
     * @see #notEmpty()
     */
    private final Object value;

    /**
     * Creates a pointer to null. Flags as empty.
     * @see #notEmpty()
     */
    public ValueHolder() {
        this(null);
    }

    /**
     * Creates a new pointer to a value
     * @param value The value to point towards. Type should be compatible with the compiler.
     */
    public ValueHolder(Object value) {
        this.value = value;
    }

    /**
     * Get the value being pointed to casted as an {@link Object}. The contents being returned by this method are mutable.
     * @return The value being pointed to, in its current state.
     */
    public Object getValue() {
        if (value instanceof ValueHolder parsed) {
            return parsed.getValue();
        }
        return value;
    }

    /**
     * Get the stored value as an {@link Object} array. If the original value was already an {@link Object} array, it wil lremain untouched. Otherwise, it will take the string format of the value and return an array of all the characters (casted to {@link String})
     * @return {@link Object} array
     * @see #getValue()
     */
    public Object[] getArray() {
        if (!notEmpty()) {
            return new Object[0];
        }
        if (isType(Object[].class)) {
            return (Object[]) getValue();
        }
        String str = getString();
        Object[] array = new Object[str.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = String.valueOf(str.charAt(i));
        }
        return array;
    }

    /**
     * Get the stored value as a {@link String}
     * @return The value casted to a {@link String}. If the value is an array, this method will take care of converting it into a string.
     * @see Arrays#toString(Object[])
     * @see String#valueOf(Object)
     */
    public String getString() {
        if (isType(Object[].class)) {
            return Arrays.toString((Object[]) getValue());
        }
        return String.valueOf(getValue());
    }

    /**
     * Get the type being pointed to
     * @return The class type
     * @see Class
     * @see Object#getClass()
     */
    public Class<?> getType() {
        Object value = getValue();
        if (value == null) {
            return NullType.class;
        }
        return value.getClass();
    }

    /**
     * Check if the type being pointed to is the same as parameter {@code type}.
     * @param type The other type
     * @return {@code true} if tne value shares the same class. {@code false} if not.
     */
    public boolean isType(Class<?> type) {
        return getType().equals(type);
    }

    /**
     * Check if this holder doesn't point to null
     * @return {@code true} if this points to a value, {@code false} if it points to {@code null}
     */
    public boolean notEmpty() {
        return getValue() != null;
    }

    /**
     * Get the value as a number. If the value is numerical, it returns the value as a {@link Double}. If it's a {@link String} or an {@code Array}, it will return its length. If it's a {@link Boolean}, {@code true} will return 1, and {@code false} will return 0.
     * @return {@link Double} representation of the value
     */
    public Double getSize() {
        if (getValue() instanceof ValueHolder holder) {
            return holder.getSize();
        }
        Class<?> type = getType();

        if (type.equals(Double.class)) {
            return (double) getValue();
        } else if (type.equals(String.class)) {
            return (double) ((String) getValue()).length();
        } else if (type.equals(Boolean.class)) {
            return ((boolean) getValue()) ? 1D : 0D;
        } else if (type.equals(Object[].class)) {
            return (double) ((Object[]) getValue()).length;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        Object value = getValue();
        if (value == null) {
            if (obj instanceof ValueHolder parsed) {
                return !parsed.notEmpty();
            }
            return false;
        }
        if (obj instanceof ValueHolder parsed) {
            return value.equals(parsed.getValue());
        }
        return value.equals(obj);
    }

    @Override
    public String toString() {
        if (isType(String.class)) {
            return "\"%s\"".formatted(((String) getValue()).replace("\"", "\\\""));
        }
        return getString();
    }
}