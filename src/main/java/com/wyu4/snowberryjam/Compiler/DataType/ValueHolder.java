package com.wyu4.snowberryjam.Compiler.DataType;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.Helpers.EnumHelper;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

public class ValueHolder {
    public static ValueHolder fromNode(JsonNode node) {
        if (Compiler.isPrimitive(node)) {
            return new ValueHolder(Compiler.asPrimitiveObject(node));
        }

        SourceId id = EnumHelper.stringToId(Compiler.getId(node));
        switch (id) {
            case VARIABLE -> {
                return new VariableReference(Compiler.getName(node));
            }
            default -> {
                Compiler.warn("Non-primitive value with ID \"{}\" is not recognized.", id);
                return new ValueHolder();
            }
        }
    }

    private final Object value;

    public ValueHolder() {
        this(null);
    }

    public ValueHolder(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return getValue().toString();
    }

    public Class<?> getType() {
        return value.getClass();
    }

    public boolean isPrimitive() {
        Class<?> type = getType();
        return type.equals(String.class) || type.equals(Boolean.class) || type.equals(Double.class);
    }

    public boolean isEmpty() {
        return getValue() == null;
    }
}