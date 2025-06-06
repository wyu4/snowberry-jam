package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.values.conditional.ConditionalHolder;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A while loop. The condition is stored as {@link SourceKey#VALUE}. Runs {@link SourceKey#BODY} while true.
 */
public class WhileTask implements ExecutableTask {
    /**
     * The condition. Must be of type {@link Boolean}.
     * @see ValueHolder#getType()
     * @see ValueHolder#checkValueIsConditional(ValueHolder)
     * @see com.wyu4.snowberryjam.compiler.data.values.conditional.SameType
     */
    private final ValueHolder condition;
    /**
     * The body to run while the condition is true
     */
    private final BodyStack body;

    /**
     * Create a new while loop
     * @param node The {@link JsonNode} to refer
     */
    public WhileTask(JsonNode node) {
        condition = ValueHolder.checkValueIsConditional(ValueHolder.fromNode(node.get(SourceKey.VALUE.toString())));
        body = new BodyStack(SourceId.WHILE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    /**
     * Create a new while loop
     * @param condition The condition to check before running {@code body}
     * @param body The body to run while the condition is {@code true}
     */
    public WhileTask(ConditionalHolder condition, BodyStack body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        while(feedback().equals(true)) {
            body.execute();
        }
    }

    /**
     * @return {@link SourceId#WHILE}
     */
    @Override
    public SourceId getId() {
        return SourceId.WHILE;
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
        return "while %s, run \"body\"".formatted(condition);
    }
}
