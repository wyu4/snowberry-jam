package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.ValueHolder;
import com.wyu4.snowberryjam.Compiler.DataType.VariableReference;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class SetTask extends CoreElement implements ExecutableTask {
    private final ValueHolder value;

    public SetTask(ValueHolder name, ValueHolder value) {
        super(SourceId.SET, name);
        this.value = value;
    }

    @Override
    public void execute() {
        LocalStorage.setVariable(getName(), feedback());
    }

    @Override
    public Object feedback() {
        return value.getValue();
    }

    @Override
    public boolean isValid() {
        return value.getType().equals(LocalStorage.getRaw(getName()).getClass());
    }

    @Override
    public String toString() {
        return "set variable \"%s\" -> %s".formatted(getName(), feedback());
    }
}
