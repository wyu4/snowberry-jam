package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Arithmetic.Plus;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.*;
import com.wyu4.snowberryjam.Compiler.Helpers.EnumHelper;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

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

        switch (id) {
            case VARIABLE -> {
                return new VariableReference(Compiler.getName(node));
            }
            case EQUALS -> {
                return new Equals(
                        fromNode(
                                node.get(SourceKey.PARAM_A.toString())
                        ),
                        fromNode(
                                node.get(SourceKey.PARAM_B.toString())
                        )
                );
            }
            case GREATER_THAN -> {
                return new GreaterThan(
                        fromNode(
                                node.get(SourceKey.PARAM_A.toString())
                        ),
                        fromNode(
                                node.get(SourceKey.PARAM_B.toString())
                        )
                );
            }
            case GREATER_OR_EQUAL_TO -> {
                return new GreaterOrEqualTo(
                        fromNode(
                                node.get(SourceKey.PARAM_A.toString())
                        ),
                        fromNode(
                                node.get(SourceKey.PARAM_B.toString())
                        )
                );
            }
            case LESS_THAN -> {
                return new LessThan(
                        fromNode(
                                node.get(SourceKey.PARAM_A.toString())
                        ),
                        fromNode(
                                node.get(SourceKey.PARAM_B.toString())
                        )
                );
            }
            case LESS_OR_EQUAL_TO -> {
                return new LessOrEqualTo(
                        fromNode(
                                node.get(SourceKey.PARAM_A.toString())
                        ),
                        fromNode(
                                node.get(SourceKey.PARAM_B.toString())
                        )
                );
            }
            case PLUS -> {
                return new Plus(
                        fromNode(
                                node.get(SourceKey.PARAM_A.toString())
                        ),
                        fromNode(
                                node.get(SourceKey.PARAM_B.toString())
                        )
                );
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

    public boolean isType(Class<?> type) {
        return getType().equals(type);
    }

    public boolean isPrimitive() {
        Class<?> type = getType();
        return type.equals(String.class) || type.equals(Boolean.class) || type.equals(Double.class);
    }

    public boolean isEmpty() {
        return getValue() == null;
    }

    public Double getSize() {
        if (getValue() instanceof ValueHolder holder) {
            return holder.getSize();
        }
        else if (isType(Double.class)) {
            return (double) getValue();
        } else if (isType(String.class)) {
            return (double) ((String) getValue()).length();
        } else if (isType(Boolean.class)) {
            return ((boolean) getValue()) ? 1D : 0D;
        }
        return null;
    }

    public Object add(ValueHolder holder) {
        if (isType(Double.class) && holder.isType(Double.class)) {
            return ((double) getValue()) + ((double) holder.getValue());
        }
        return getString() + holder.getString();
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