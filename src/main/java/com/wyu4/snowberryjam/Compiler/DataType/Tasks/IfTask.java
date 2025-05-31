package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.ConditionalHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class IfTask extends CoreElement implements ExecutableTask {
    private final ConditionalHolder condition;
    private final BodyStack body;

    public IfTask(JsonNode node) {
        super(SourceId.IF);

        ValueHolder holder = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));

        if (holder instanceof ConditionalHolder) {
            condition = (ConditionalHolder) holder;
            body = new BodyStack(SourceId.IF);
            Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
        } else {
            throw new IllegalArgumentException("Value passed as condition is not conditional.");
        }
    }

    public IfTask(ConditionalHolder condition, BodyStack body) {
        super(SourceId.IF);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        if (condition.getState()) {
            body.execute();
        }
    }

    @Override
    public String toString() {
        return "run \"body\" if %s".formatted(condition);
    }
}
