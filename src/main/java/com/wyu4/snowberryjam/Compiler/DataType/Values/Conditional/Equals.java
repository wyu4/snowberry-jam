package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class Equals extends ConditionalHolder {
    public Equals(ValueHolder a, ValueHolder b) {
        super(a, b);
    }

    @Override
    public Boolean getState() {
        return getA().equals(getB());
    }

    @Override
    public String toString() {
        return "%s equals %s".formatted(getA(), getB());
    }
}
