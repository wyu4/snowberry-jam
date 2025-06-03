package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

/**
 * A standard {@link ValueHolder} that takes two or more arguments.
 * @see SourceKey#PARAM_A
 * @see SourceKey#PARAM_B
 */
public class InteractiveValueHolder extends ValueHolder {
    /**
     * Parameter A
     */
    private final ValueHolder a;
    /**
     * Parameter B
     */
    private final ValueHolder b;

    /**
     * Create a new Interactive Value Holder
     * @param node The {@link JsonNode} to refer
     */
    public InteractiveValueHolder(JsonNode node) {
        this(
                fromNode(node.get(SourceKey.PARAM_A.toString())),
                fromNode(node.get(SourceKey.PARAM_B.toString()))
        );
    }

    /**
     * Create a new Interactive Value Holder
     * @param a Parameter A.
     * @param b Parameter B.
     */
    public InteractiveValueHolder(ValueHolder a, ValueHolder b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Get the pointer to parameter A
     * @return The pointer to parameter A
     */
    protected ValueHolder getA() {
        return a;
    }

    /**
     * Get the pointer to parameter B
     * @return The pointer to parameter B
     */
    protected ValueHolder getB() {
        return b;
    }
}