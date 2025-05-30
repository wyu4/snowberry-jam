package com.wyu4.snowberryjam.Compiler.DataType;

public class ValueHolder {
    private final Object value;

    public ValueHolder(Object value) {
        this.value = value;
    }

    public Object getValue() {
        if (value instanceof VariableReference<?> parsed) {
            return parsed.getValue();
        }
        return value;
    }
}