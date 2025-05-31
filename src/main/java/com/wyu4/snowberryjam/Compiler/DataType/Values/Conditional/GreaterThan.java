package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class GreaterThan extends ConditionalHolder {
    public GreaterThan(ValueHolder a, ValueHolder b) {
        super(a, b);
    }

    @Override
    public Boolean getState() {
        Double sizeA = getA().getSize();
        Double sizeB = getB().getSize();
        if (sizeA == null || sizeB == null) {
            return false;
        }
        return sizeA > sizeB;
    }

    @Override
    public String toString() {
        return "if %s is greater than %s".formatted(getA(), getB());
    }
}
