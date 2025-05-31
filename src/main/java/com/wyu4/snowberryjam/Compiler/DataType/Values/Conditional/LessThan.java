package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class LessThan extends ConditionalHolder {
    public LessThan(ValueHolder a, ValueHolder b) {
        super(a, b);
    }

    @Override
    public Boolean getState() {
        Double sizeA = getA().getSize();
        Double sizeB = getB().getSize();
        if (sizeA == null || sizeB == null) {
            return false;
        }
        return sizeA < sizeB;
    }

    @Override
    public String toString() {
        return "%s is less than %s".formatted(getA(), getB());
    }
}
