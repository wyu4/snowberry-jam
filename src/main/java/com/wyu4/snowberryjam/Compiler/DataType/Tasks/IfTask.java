package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Enums.SourceId;
import com.wyu4.snowberryjam.Compiler.Enums.SourceKey;

/**
 * An if-else statement. The condition is stored as {@link SourceKey#VALUE}. Runs {@link SourceKey#BODY} if true.
 */
public class IfTask implements ExecutableTask {
    /**
     * The condition. Must be of type {@link Boolean}.
     * @see ValueHolder#getType()
     * @see ValueHolder#checkValueIsConditional(ValueHolder)
     * @see com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.SameType
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
}
