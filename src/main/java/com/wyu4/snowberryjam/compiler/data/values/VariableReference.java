package com.wyu4.snowberryjam.compiler.data.values;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * A pointer to a variable stored in {@link LocalStorage}
 */
public class VariableReference extends ValueHolder{
    /**
     * The name of the variable
     */
    private final ValueHolder name;

    /**
     * Create a new Vairable Reference
     * @param name The name of the variable as a String
     */
    public VariableReference(String name) {
        this(new ValueHolder(name));
    }

    /**
     * Create a new Vairable Reference
     * @param name The name of the variable as a {@link ValueHolder}
     */
    public VariableReference(ValueHolder name) {
        this.name = name;
    }

    /**
     * Get the name of the variable being pointed to
     * @return Name
     */
    public ValueHolder getName() {
        return name;
    }

    /**
     * Get the value of the variable being pointed to
     * @return The value of the variable as an {@link Object}
     */
    @Override
    public Object getValue() {
        Object raw = LocalStorage.getRaw(getName().getString());
        if (raw instanceof Object[] parsed) {
            return parsed.clone();
        }
        return raw;
    }

    /**
     * @return {@link SourceId#VARIABLE}
     */
    @Override
    public SourceId getId() {
        return SourceId.VARIABLE;
    }

    @Override
    public String toString() {
        return "the value of variable \"%s\"".formatted(getName());
    }
}
