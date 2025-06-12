package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.BodiedTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ValuedTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * An if-else statement. The condition is stored as {@link SourceKey#VALUE}. Runs {@link SourceKey#BODY} if true.
 */
public class IfTask implements ExecutableTask, BodiedTask, ValuedTask {
    /**
     * The condition. Must be of type {@link Boolean}.
     * @see ValueHolder#getType()
     * @see ValueHolder#checkValueIsConditional(ValueHolder)
     * @see com.wyu4.snowberryjam.compiler.data.values.conditional.SameType
     */
    private final ValueHolder condition;
    /**
     * The body to run if the condition is true
     */
    private final BodyStack body;

    /**
     * Create a new if-else statement
     * @param node The {@link JsonNode} to refer
     */
    public IfTask(JsonNode node) {
        this.condition = ValueHolder.checkValueIsConditional(
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()))
        );
        this.body = new BodyStack(SourceId.IF);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }
        
        if (feedback().equals(true)) {
            body.execute();
        }
    }

    /**
     * @return {@link SourceId#IF}
     */
    @Override
    public SourceId getId() {
        return SourceId.IF;
    }

    /**
     * @return The current state of the condition.
     */
    @Override
    public Object feedback() {
        return condition.getValue().equals(true);
    }

    @Override
    public String toString() {
        return "run \"body\" if %s".formatted(condition);
    }

    @Override
    public BodyStack getBody() {
        return body;
    }

    @Override
    public ValueHolder getValue() {
        return condition;
    }
}
