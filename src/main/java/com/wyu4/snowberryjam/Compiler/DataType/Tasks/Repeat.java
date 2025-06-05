package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Enums.SourceId;
import com.wyu4.snowberryjam.Compiler.Enums.SourceKey;

/**
 * A repeat loop, often times seen as a {@code for-loop} in other languages. The number of times to iterate is stored as {@link SourceKey#VALUE}, and the body to run on each iteration is stored as {@link SourceKey#BODY}.
 */
public class Repeat implements ExecutableTask {
    /**
     * The condition. Must be of type {@link Boolean}.
     * @see ValueHolder#getType()
     * @see ValueHolder#checkValueIsConditional(ValueHolder)
     * @see com.wyu4.snowberryjam.Compiler.DataType.Values.Conditional.SameType
     */
    private final ValueHolder condition;
    private final BodyStack body;

    public Repeat(JsonNode node) {
        this.condition = ValueHolder.checkValueIsConditional(
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()))
        );
        this.body = new BodyStack(SourceId.WHILE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    @Override
    public void execute() {
        for (int i = 0; i < condition.getSize(); i++) {
            body.execute();
        }
    }

    @Override
    public SourceId getId() {
        return SourceId.REPEAT;
    }

    /**
     * @return null.
     */
    @Override
    public Object feedback() {
        return null;
    }

    @Override
    public String toString() {
        return "run \"body\" %s (or %s) times".formatted(condition, condition.getSize());
    }
}
