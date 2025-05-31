package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class ConditionalHolder extends ValueHolder {
    private final ValueHolder a;
    private final ValueHolder b;

    public ConditionalHolder(ValueHolder a, ValueHolder b) {
        this.a = a;
        this.b = b;
    }

    protected ValueHolder getA() {
        return a;
    }

    protected ValueHolder getB() {
        return b;
    }

    public Boolean getState() {
        return true;
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
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return "default unset conditional holder with parameters %s and %s".formatted(getA(), getB());
    }
}
