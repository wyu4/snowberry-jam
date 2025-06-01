package com.wyu4.snowberryjam.Compiler.DataType.Values.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class ArrayHolder extends InteractiveValueHolder {
    private final ValueHolder i;

    public ArrayHolder(JsonNode node) {
        super(node);
        i = fromNode(node.get(SourceKey.PARAM_I.toString()));
    }

    protected ValueHolder getI() {
        return i;
    }

    protected int getValidIndex() {
        Class<?> type = i.getType();
        if (!type.equals(Double.class)) {
            throw new IllegalArgumentException("Argument \"b\" %s (%s) is not a number.".formatted(getB().getString(), type.getCanonicalName()));
        }

        return (int) Math.round((double) getI().getValue());
    }
}
