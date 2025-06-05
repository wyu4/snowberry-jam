package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class SetTask implements ExecutableTask {
    private final ValueHolder name, value;

    public SetTask(JsonNode node) {
        this(
                ValueHolder.fromNode(node.get(SourceKey.NAME.toString())),
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()))
        );
    }

    public SetTask(ValueHolder name, ValueHolder value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void execute() {
        LocalStorage.setVariable(name.getString(), feedback());
    }

    @Override
    public SourceId getId() {
        return SourceId.SET;
    }

    @Override
    public Object feedback() {
        return value.getValue();
    }

    @Override
    public String toString() {
        return "set variable %s to %s".formatted(name, value);
    }
}
