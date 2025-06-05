package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.ConditionalHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class IfElseTask implements ExecutableTask {
    private final ValueHolder condition;
    private final BodyStack body, elseBody;

    public IfElseTask(JsonNode node) {

        condition = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));

        if (!condition.isType(Boolean.class)) {
            throw new IllegalArgumentException("Value passed as condition is not conditional.");
        }
        body = new BodyStack(SourceId.IF_ELSE);
        elseBody = new BodyStack(SourceId.IF_ELSE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
        Compiler.compileBody(node.get(SourceKey.ELSE.toString()), elseBody);
    }

    public IfElseTask(ConditionalHolder condition, BodyStack body, BodyStack elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    @Override
    public void execute() {
        if (condition.getValue().equals(true)) {
            body.execute();
        } else {
            elseBody.execute();
        }
    }

    @Override
    public SourceId getId() {
        return SourceId.IF_ELSE;
    }

    @Override
    public String toString() {
        return "run \"body\" if %s, otherwise run \"else\" ";
    }
}
