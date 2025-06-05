package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.ConditionalHolder;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class WhileTask implements ExecutableTask {
    private final ConditionalHolder condition;
    private final BodyStack body;

    public WhileTask(JsonNode node) {
        ValueHolder holder = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));
        if (holder instanceof ConditionalHolder) {
            condition = (ConditionalHolder) holder;
            body = new BodyStack(SourceId.WHILE);
            Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
        } else {
            throw new IllegalArgumentException("Value passed as condition is not conditional.");
        }
    }

    public WhileTask(ConditionalHolder condition, BodyStack body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        while(condition.getState()) {
            body.execute();
        }
    }

    @Override
    public SourceId getId() {
        return SourceId.WHILE;
    }

    @Override
    public String toString() {
        return "while %s, run \"body\"".formatted(condition);
    }
}
