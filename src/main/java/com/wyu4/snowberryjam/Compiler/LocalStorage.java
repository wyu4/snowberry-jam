package com.wyu4.snowberryjam.Compiler;

import java.util.HashMap;

public abstract class LocalStorage {
    private static final HashMap<String, Object> VARIABLES = new HashMap<>();

    public static Object getRaw(String name) throws NullPointerException {
        Object currentValue = VARIABLES.get(name);
        if (currentValue == null) {
            throw new NullPointerException("No variable with the name \"" + name + "\" was found.");
        }
        return currentValue;
    }

    public static void flush() {
        VARIABLES.clear();
    }

    public static void setVariable(String name, Object newValue) throws NullPointerException {
        Object currentValue = getRaw(name);
        if (currentValue.getClass().equals(newValue.getClass())) {
            VARIABLES.put(name, newValue);
        }
    }

    protected static void createVariable(String name, Object value) throws IllegalStateException {
        if (VARIABLES.containsKey(name)) {
            throw new IllegalStateException("Variable name \"" + name + "\" is already occupied.");
        }
        VARIABLES.put(name, value);
    }
}
