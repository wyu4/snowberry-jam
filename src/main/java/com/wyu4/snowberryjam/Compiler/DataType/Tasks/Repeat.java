package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class Repeat implements ExecutableTask {
    private final ValueHolder value;
    private final BodyStack body;

    public Repeat(JsonNode node) {

        value = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));
        body = new BodyStack(SourceId.WHILE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    public Repeat(ValueHolder value, BodyStack body) {
        this.value = value;
        this.body = body;
    }

    @Override
    public void execute() {
        for (int i = 0; i < value.getSize(); i++) {
            body.execute();
        }
    }

    @Override
    public SourceId getId() {
        return SourceId.REPEAT;
    }

    @Override
    public String toString() {
        return "run \"body\" %s (or %s) times".formatted(value, value.getSize());
    }
}
