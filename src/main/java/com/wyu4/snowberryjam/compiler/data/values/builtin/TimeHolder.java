package com.wyu4.snowberryjam.compiler.data.values.builtin;

import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

/**
 * Value Holder that provides time resources.
 */
public class TimeHolder extends ValueHolder {
    /**
     * @return The current time since the midnight of January 1, 1970 UTC, in milliseconds
     * @see System#currentTimeMillis()
     */
    @Override
    public Object getValue() {
        return (double) System.currentTimeMillis();
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
        return "the current time in milliseconds";
    }
}
