package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.VariableReference;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class SetTask extends CoreElement implements ExecutableTask {
    private final Object value;

    public SetTask(String name, Object value) {
        super(SourceId.SET, name);
        this.value = value;
    }

    @Override
    public void execute() {
        LocalStorage.setVariable(getName(), feedback());
    }

    @Override
    public Object feedback() {
        if (value instanceof VariableReference<?> reference) {
            return reference.getValue();
        }
        return value;
    }

    @Override
    public boolean isValid() {
        Object rawValue = LocalStorage.getRaw(getName()).getClass();
        if (value instanceof VariableReference<?> reference) {
            return reference.isValid();
        }
        return value.getClass().equals(rawValue);
    }
}
