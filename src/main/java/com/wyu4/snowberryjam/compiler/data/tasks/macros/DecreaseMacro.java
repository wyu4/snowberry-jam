package com.wyu4.snowberryjam.compiler.data.tasks.macros;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.tasks.SetTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.VariableReference;
import com.wyu4.snowberryjam.compiler.data.values.math.Minus;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A variable increaser statement. The name of the variable is stored as
 * {@link SourceKey#NAME}, and the value to decrease by is stored as
 * {@link SourceKey#VALUE}.
 */
public class DecreaseMacro extends SetTask {
    /**
     * Create a new decrease statement
     * 
     * @param node The {@link JsonNode} to refer
     */
    public DecreaseMacro(JsonNode node) {
        this(
                ValueHolder.fromNode(node.get(SourceKey.NAME.toString())),
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString())));
    }

    /**
     * Create a new decrease statement
     * 
     * @param name  The name of the variable
     * @param value The value to decrease the variable by
     */
    public DecreaseMacro(ValueHolder name, ValueHolder value) {
        super(name, new Minus(new VariableReference(name), value));
    }
}
