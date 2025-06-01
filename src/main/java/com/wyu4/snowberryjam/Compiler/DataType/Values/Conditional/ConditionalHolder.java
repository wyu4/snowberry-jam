package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;

public class ConditionalHolder extends InteractiveValueHolder {
    public ConditionalHolder(JsonNode node) {
        super(node);
    }

    public Boolean getState() {
        return true;
    }

    public boolean bothTypesAre(Class<?> type) {
        return getA().isType(type) && getB().isType(type);
    }

    @Override
    public Object getValue() {
        return getState();
    }

    @Override
    public Class<?> getType() {
        return Boolean.class;
    }

    @Override
    public boolean notEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "default unset conditional holder with parameters %s and %s".formatted(getA(), getB());
    }
}
