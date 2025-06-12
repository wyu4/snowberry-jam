package com.wyu4.snowberryjam.compiler.data.values.math;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

/**
 * A default unset arithmetic holder. This handles any math operations
 */
public class ArithmeticHolder extends InteractiveValueHolder {
    /**
     * Create a new Arithmetic Holder
     * @param node The {@link JsonNode} to refer
     */
    public ArithmeticHolder(JsonNode node) {
        super(node);
    }

    /**
     * Create a new Arithmetic Holder
     * @param a Value A
     * @param b Value B
     */
    public ArithmeticHolder(Object a, Object b) {
        this(new ValueHolder(a), new ValueHolder(b));
    }

    /**
     * Create a new Arithmetic Holder
     * @param a Value A
     * @param b Value B
     */
    public ArithmeticHolder(ValueHolder a, ValueHolder b) {
        super(a, b);
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
