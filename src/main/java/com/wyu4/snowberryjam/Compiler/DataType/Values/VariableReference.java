package com.wyu4.snowberryjam.Compiler.DataType.Values;

import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class VariableReference extends ValueHolder{
    private final String name;

    public VariableReference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return LocalStorage.getRaw(getName());
    }

    @Override
    public Class<?> getType() {
        return getValue().getClass();
    }

    @Override
    public String toString() {
        return "the value of variable \"%s\"".formatted(getName());
    }
}
