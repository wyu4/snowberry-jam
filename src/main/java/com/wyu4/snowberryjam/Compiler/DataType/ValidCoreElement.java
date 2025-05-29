package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

public class ValidCoreElement extends CoreElement {
    public ValidCoreElement(SourceId id) {
        super(id);
    }

    public ValidCoreElement(SourceId id, String name) {
        super(id, name);
    }

    @Override
    boolean isValid() {
        return true;
    }
}
