package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.ConditionalHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class IfTask implements ExecutableTask {
    private final ValueHolder condition;
    private final BodyStack body;

    public IfTask(JsonNode node) {

        condition = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));

        if (!condition.isType(Boolean.class)) {
            throw new IllegalArgumentException("Value passed as condition is not conditional.");
        }
        body = new BodyStack(SourceId.IF);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    public IfTask(ConditionalHolder condition, BodyStack body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        if (condition.getValue().equals(true)) {
            body.execute();
        }
    }

    @Override
    public SourceId getId() {
        return SourceId.IF;
    }

    @Override
    public String toString() {
        return "run \"body\" if %s".formatted(condition);
    }
}
