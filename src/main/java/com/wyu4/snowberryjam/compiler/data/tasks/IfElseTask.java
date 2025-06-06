package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * An if-else statement. The condition is stored as {@link SourceKey#VALUE}. Runs {@link SourceKey#BODY} if true, else {@link SourceKey#ELSE}.
 */
public class IfElseTask implements ExecutableTask {
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
     * The body to run if the condition is false
     */
    private final BodyStack elseBody;

    /**
     * Create a new if-else statement
     * @param node The {@link JsonNode} to refer
     */
    public IfElseTask(JsonNode node) {
        this.condition = ValueHolder.checkValueIsConditional(
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()))
        );
        this.body = new BodyStack(SourceId.IF_ELSE);
        this.elseBody = new BodyStack(SourceId.IF_ELSE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
        Compiler.compileBody(node.get(SourceKey.ELSE.toString()), elseBody);
    }

    @Override
    public void execute() {
        if (feedback().equals(true)) {
            body.execute();
        } else {
            elseBody.execute();
        }
    }

    /**
     * @return {@link SourceId#IF_ELSE}
     */
    @Override
    public SourceId getId() {
        return SourceId.IF_ELSE;
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
        return "run \"body\" if %s, otherwise run \"else\" ";
    }
}
