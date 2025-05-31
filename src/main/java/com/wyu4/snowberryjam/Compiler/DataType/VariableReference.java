package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class VariableReference extends ValueHolder{
    public VariableReference(String name) {
        super(name);
    }

    public String getName() {
        return super.getValue().toString();
    }

    @Override
    public Object getValue() {
        return LocalStorage.getRaw(getName());
    }

    @Override
    public Class<?> getType() {
        return getValue().getClass();
    }
}
