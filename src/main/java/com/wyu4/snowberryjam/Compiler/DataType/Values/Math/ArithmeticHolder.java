package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;

public class ArithmeticHolder extends InteractiveValueHolder {
    public ArithmeticHolder(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        return 0;
    }

    @Override
    public boolean notEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "default unset arithmetic holder with parameters %s and %s".formatted(getA(), getB());
    }
}
