package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

public class Not extends ConditionalHolder {

    public Not(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        Class<?> type = getA().getType();
        if (type.equals(Boolean.class)) {
            return !((boolean) getA().getValue());
        } else if (type.equals(Double.class)) {
            return !((double) getA().getValue() > 0);
        }

        throw new IllegalArgumentException("Cannot invert type [%s]".formatted(type.getCanonicalName()));
    }

    @Override
    public String toString() {
        return "not %s".formatted(getA());
    }
}
