package com.wyu4.snowberryjam.Compiler.DataType;

public class ValidCoreElement extends CoreElement {
    public ValidCoreElement(CoreId id) {
        super(id);
    }

    public ValidCoreElement(CoreId id, String name) {
        super(id, name);
    }

    @Override
    boolean isValid() {
        return true;
    }
}
