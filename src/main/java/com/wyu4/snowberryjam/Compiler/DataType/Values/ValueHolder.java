package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Array.ElementAtIndex;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Array.WithUpdatedElement;
import com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn.RandomHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn.TimeHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.*;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conversion.ArrayOf;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conversion.SizeOf;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Math.*;
import com.wyu4.snowberryjam.Compiler.Helpers.EnumHelper;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

import javax.lang.model.type.NullType;
import java.util.Arrays;

public class ValueHolder {

    private static boolean isPrimitive(JsonNode node) {
        return node.isTextual() || node.isBoolean() || node.isNumber() || node.isArray();
    }

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

    private final Object value;

    public ValueHolder() {
        this(null);
    }

    public ValueHolder(Object value) {
        this.value = value;
    }

    public Object getValue() {
        if (value instanceof ValueHolder parsed) {
            return parsed.getValue();
        }
        return value;
    }

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

    public String getString() {
        if (isType(Object[].class)) {
            return Arrays.toString((Object[]) getValue());
        }
        return String.valueOf(getValue());
    }

    public Class<?> getType() {
        Object value = getValue();
        if (value == null) {
            return NullType.class;
        }
        return value.getClass();
    }

    public boolean isType(Class<?> type) {
        return getType().equals(type);
    }

    public boolean notEmpty() {
        return getValue() != null;
    }

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
        if (obj instanceof ValueHolder parsed) {
            return getValue().equals(parsed.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        if (isType(String.class)) {
            return "\"%s\"".formatted(getValue());
        }
        return getString();
    }
}