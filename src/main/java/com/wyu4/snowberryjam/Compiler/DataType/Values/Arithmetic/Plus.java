package com.wyu4.snowberryjam.Compiler.DataType.Values.Arithmetic;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class Plus extends ArithmeticHolder {
    public Plus(ValueHolder a, ValueHolder b) {
        super(a, b);
    }

    @Override
    public Object getValue() {
        return getA().add(getB());
    }
}
