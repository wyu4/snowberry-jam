package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn.Random;
import com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn.TimeHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.*;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Math.*;
import com.wyu4.snowberryjam.Compiler.Helpers.EnumHelper;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

public class ValueHolder {
    public static ValueHolder fromNode(JsonNode node) {
        if (node == null) {
            return new ValueHolder();
        }

        if (Compiler.isPrimitive(node)) {
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
            case PLUS -> new Plus(node);
            case MINUS -> new Minus(node);
            case MULTIPLY -> new Multiply(node);
            case DIVIDE -> new Divide(node);
            case MODULUS -> new Modulus(node);
            case AND -> new And(node);
            case OR -> new Or(node);
            case NOT -> new Not(node);
            case TIME -> new TimeHolder();
            case RANDOM -> new Random();
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

    public String getString() {
        return getValue().toString();
    }

    public Class<?> getType() {
        return getValue().getClass();
    }

    public boolean isType(Class<?> type) {
        return getType().equals(type);
    }

    public boolean isPrimitive() {
        Class<?> type = getType();
        return type.equals(String.class) || type.equals(Boolean.class) || type.equals(Double.class);
    }

    public boolean notEmpty() {
        return getValue() != null;
    }

    public Double getSize() {
        if (getValue() instanceof ValueHolder holder) {
            return holder.getSize();
        } else if (isType(Double.class)) {
            return (double) getValue();
        } else if (isType(String.class)) {
            return (double) ((String) getValue()).length();
        } else if (isType(Boolean.class)) {
            return ((boolean) getValue()) ? 1D : 0D;
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
        return getValue().toString();
    }
}