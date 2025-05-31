package com.wyu4.snowberryjam.Compiler.DataType.Values.Math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class Plus extends ArithmeticHolder {

    public Plus(JsonNode node) {
        super(node);
    }

    @Override
    public Object getValue() {
        return getA().add(getB());
    }

    @Override
    public String toString() {
        return "%s + %s".formatted(getA(), getB());
    }
}
