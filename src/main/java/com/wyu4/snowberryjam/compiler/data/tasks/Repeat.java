package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A repeat loop, similar to a {@code for-loop}. The number of times to iterate
 * is stored as {@link SourceKey#VALUE}, and the body to run on each iteration
 * is stored as {@link SourceKey#BODY}. An optional variable to set each
 * iteration can be stored as {@link SourceKey#NAME}.
 */
public class Repeat implements ExecutableTask {
    /**
     * The number of times to repeat the loop. Can be any type, so long as it has a
     * size.
     * 
     * @see ValueHolder#getSize()
     */
    private final ValueHolder repeats;

    /**
     * The variable to set. Can be empty, so this is purely optional.
     * 
     * @see ValueHolder#getSize()
     */
    private final ValueHolder variableName;

    /**
     * The body to run
     */
    private final BodyStack body;

    /**
     * Create a new repeat loop
     * 
     * @param node The {@link JsonNode} to refer
     */
    public Repeat(JsonNode node) {
        this.repeats = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));
        this.variableName = ValueHolder.fromNode(node.get(SourceKey.NAME.toString()));
        this.body = new BodyStack(SourceId.WHILE);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    @Override
    public void execute() {
        final boolean setVariable = variableName.notEmpty();
        for (int i = 0; i < repeats.getSize(); i++) {
            if (setVariable) {
                LocalStorage.setVariable(variableName.getString(), i);
            }
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
        return "run \"body\" %s (or %s) times".formatted(repeats, repeats.getSize());
    }
}
