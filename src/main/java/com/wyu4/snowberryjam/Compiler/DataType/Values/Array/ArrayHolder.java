package com.wyu4.snowberryjam.Compiler.DataType.Values.Array;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.InteractiveValueHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Enums.SourceKey;

/**
 * Default unset array holder. This is equivalent to an {@link InteractiveValueHolder}, but with an extra parameter {@code I}.
 * @see SourceKey#PARAM_I
 */
public class ArrayHolder extends InteractiveValueHolder {
    /**
     * Parameter I
     */
    private final ValueHolder i;

    /**
     * Create a new Array Holder
     * @param node The {@link JsonNode} to refer
     */
    public ArrayHolder(JsonNode node) {
        super(node);
        i = fromNode(node.get(SourceKey.PARAM_I.toString()));
    }

    /**
     * Get the pointer to parameter I
     * @return The pointer to parameter I
     */
    protected ValueHolder getI() {
        return i;
    }

    /**
     * Converts the value of I to a valid and usable integer, in the context of array indexes.
     * @return Parameter I as an {@link Integer}
     * @see #getI()
     * @throws IllegalArgumentException if value I is not a number.
     */
    protected int getValidIndex() {
        Class<?> type = i.getType();
        if (!type.equals(Double.class)) {
            throw new IllegalArgumentException("Argument \"b\" %s (%s) is not a number.".formatted(getB().getString(), type.getCanonicalName()));
        }

        return (int) Math.round((double) getI().getValue());
    }

    @Override
    public String toString() {
        return "default unset array holder with parameters %s, %s and %s".formatted(getA(), getB(), getI());
    }
}
