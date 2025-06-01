package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class Repeat extends CoreElement implements ExecutableTask {
    private final ValueHolder value;
    private final BodyStack body;

    public Repeat(JsonNode node) {
        super(SourceId.REPEAT);

        value = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));
        body = new BodyStack(SourceId.WHILE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    public Repeat(ValueHolder value, BodyStack body) {
        super(SourceId.REPEAT);
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
    public String toString() {
        return "run \"body\" %s (or %s) times".formatted(value, value.getSize());
    }
}
