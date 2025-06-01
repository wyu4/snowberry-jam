package com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class RandomHolder extends ValueHolder {
    @Override
    public Object getValue() {
        return Math.random();
    }

    @Override
    public Class<?> getType() {
        return Double.class;
    }

    @Override
    public String toString() {
        return "random number between 0 and 1";
    }
}
