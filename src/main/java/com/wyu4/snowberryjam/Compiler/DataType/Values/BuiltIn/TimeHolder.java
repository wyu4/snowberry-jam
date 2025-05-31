package com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class TimeHolder extends ValueHolder {
    @Override
    public Object getValue() {
        return (double) System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "the current time in milliseconds";
    }
}
