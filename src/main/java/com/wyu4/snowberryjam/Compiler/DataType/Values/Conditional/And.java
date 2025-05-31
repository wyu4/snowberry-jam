package com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional;

import com.fasterxml.jackson.databind.JsonNode;

public class And extends ConditionalHolder {

    public And(JsonNode node) {
        super(node);
    }

    @Override
    public Boolean getState() {
        if (bothTypesAre(Boolean.class)) {
            return ((boolean) getA().getValue()) && ((boolean) getB().getValue());
        } else if (bothTypesAre(Double.class)) {
            return ((double) getA().getValue() > 0D) && ((double) getB().getValue() > 0D);
        } else if (bothTypesAre(String.class)) {
            return !((String) getA().getValue()).isEmpty() && !((String) getB().getValue()).isEmpty();
        }
        return getA().notEmpty() && getB().notEmpty();
    }

    @Override
    public String toString() {
        return "%s and %s are true".formatted(getA(), getB());
    }
}
