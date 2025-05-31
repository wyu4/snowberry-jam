package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class SetTask extends CoreElement implements ExecutableTask {
    private final ValueHolder value;

    public SetTask(JsonNode node) {
        this(
                ValueHolder.fromNode(node.get(SourceKey.NAME.toString())),
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()))
        );
    }

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
