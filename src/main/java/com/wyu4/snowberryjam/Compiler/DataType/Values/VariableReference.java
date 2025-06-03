package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.wyu4.snowberryjam.Compiler.LocalStorage;

/**
 * A pointer to a variable stored in {@link LocalStorage}
 */
public class VariableReference extends ValueHolder{
    /**
     * The name of the variable
     */
    private final String name;

    /**
     * Create a new Vairable Reference
     * @param name The name of the variable
     */
    public VariableReference(String name) {
        this.name = name;
    }

    /**
     * Get the name of the variable being pointed to
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of the variable being pointed to
     * @return The value of the variable as an {@link Object}
     */
    @Override
    public Object getValue() {
        return LocalStorage.getRaw(getName());
    }

    @Override
    public String toString() {
        return "the value of variable \"%s\"".formatted(getName());
    }
}
