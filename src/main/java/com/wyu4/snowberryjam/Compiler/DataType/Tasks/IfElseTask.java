package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.ConditionalHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class IfElseTask extends CoreElement implements ExecutableTask {
    private final ConditionalHolder condition;
    private final BodyStack body, elseBody;

    public IfElseTask(JsonNode node) {
        super(SourceId.IF_ELSE);

        ValueHolder holder = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));

        if (holder instanceof ConditionalHolder) {
            condition = (ConditionalHolder) holder;
            body = new BodyStack(SourceId.IF_ELSE);
            elseBody = new BodyStack(SourceId.IF_ELSE);
            Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
            Compiler.compileBody(node.get(SourceKey.ELSE.toString()), elseBody);
        } else {
            throw new IllegalArgumentException("Value passed as condition is not conditional.");
        }
    }

    public IfElseTask(ConditionalHolder condition, BodyStack body, BodyStack elseBody) {
        super(SourceId.IF);
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    @Override
    public void execute() {
        if (condition.getState()) {
            body.execute();
        } else {
            elseBody.execute();
        }
    }
}
