package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class GreaterOrEqualTo extends ConditionalHolder {
    public GreaterOrEqualTo(ValueHolder a, ValueHolder b) {
        super(a, b);
    }

    @Override
    public Boolean getState() {
        Double sizeA = getA().getSize();
        Double sizeB = getB().getSize();
        if (sizeA == null || sizeB == null) {
            return false;
        }
        return sizeA >= sizeB;
    }

    @Override
    public String toString() {
        return "%s is greater or equal to %s".formatted(getA(), getB());
    }
}
