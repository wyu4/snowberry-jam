package com.wyu4.snowberryjam.Compiler.DataType.Values.Arithmetic;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class ArithmeticHolder extends ValueHolder {
    private final ValueHolder a;
    private final ValueHolder b;

    public ArithmeticHolder(ValueHolder a, ValueHolder b) {
        this.a = a;
        this.b = b;
    }

    protected ValueHolder getA() {
        return a;
    }

    protected ValueHolder getB() {
        return b;
    }

    @Override
    public Object getValue() {
        return 0;
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
        return "default unset arithmetic holder with parameters %s and %s".formatted(getA(), getB());
    }
}
