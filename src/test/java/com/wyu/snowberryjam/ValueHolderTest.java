package com.wyu.snowberryjam;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueHolderTest {

    @BeforeEach
    void setUp() {
        LocalStorage.flush();
    }

    @Test
    void testValueHolder() {
        testValueHolder(new ValueHolder(false), false, Boolean.class, 0D);
        testValueHolder(new ValueHolder(true), true, Boolean.class, 1D);
        testValueHolder(new ValueHolder(0D), 0D, Double.class, 0D);
        testValueHolder(new ValueHolder(1D), 1D, Double.class, 1D);
        testValueHolder(new ValueHolder("This is a test."), "This is a test.", String.class, 15D);
        testValueHolder(new ValueHolder(new Object[] {1, "hi"}), new Object[] {1, "hi"}, Object[].class, 2D);
    }

    private void testValueHolder(ValueHolder value, Object actualValue, Class<?> actualType, Double actualSize) {
        assertEquals(value.getType(), actualType);
        assertEquals(value.getSize(), actualSize);
        assertTrue(value.pointsToSameValueAs(actualValue));
        assertFalse(value.pointsToSameValueAs(actualValue + "append"));
        assertNotEquals(value.toString(), null);
    }
}