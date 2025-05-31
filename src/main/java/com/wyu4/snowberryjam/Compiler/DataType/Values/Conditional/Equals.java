package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

public class Equals extends ConditionalHolder {

    public Equals(JsonNode node) {
        super(node);
    }

    @Override
    public Boolean getState() {
        return getA().equals(getB());
    }

    @Override
    public String toString() {
        return "%s equal %s".formatted(getA(), getB());
    }
}
