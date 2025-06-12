package com.wyu4.snowberryjam.compiler.data.values.builtin;

import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

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
     * @return {@link SourceId#RANDOM}
     */
    @Override
    public SourceId getId() {
        return SourceId.RANDOM;
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
