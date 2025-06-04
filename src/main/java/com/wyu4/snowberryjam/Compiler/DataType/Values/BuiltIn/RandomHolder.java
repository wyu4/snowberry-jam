package com.wyu4.snowberryjam.Compiler.DataType.Values.BuiltIn;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;

/**
 * Value Holder that handles randomization
 */
public class RandomHolder extends ValueHolder {

    /**
     * @return A random {@link Double} between 0 and 1
     * @see Math#random()
     */
    @Override
    public Object getValue() {
        return Math.random();
    }

    /**
     * @return Always returns {@link Double}
     */
    @Override
    public Class<?> getType() {
        return Double.class;
    }

    @Override
    public String toString() {
        return "random number between 0 and 1";
    }
}
