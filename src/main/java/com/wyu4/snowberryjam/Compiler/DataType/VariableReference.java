package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class VariableReference<A> extends CoreElement {
    private final Class<A> type;

    public VariableReference(String name, Class<A> type) {
        super(SourceId.VARIABLE, name);
        this.type = type;
    }

    private Object getRaw() throws NullPointerException {
        return LocalStorage.getRaw(getName());
    }

    public A getValue() throws NullPointerException, ClassCastException {
        return type.cast(getRaw());
    }

    public Class<A> getType() {
        return type;
    }

    @Override
    public boolean isValid() {
        try {
            return type.isInstance(getRaw());
        } catch (Exception e) {
            Compiler.error("Variable \"" + getName() + "\" reference validation failed.", e);
            return false;
        }
    }

    @Override
    public String toString() {
        return ("{" +
                "\"%s\":\"%s\"," +
                "\"%s\":\"%s\"" +
                "}").formatted(SourceKey.ID, SourceId.VARIABLE, SourceKey.NAME, getName());
    }
}
