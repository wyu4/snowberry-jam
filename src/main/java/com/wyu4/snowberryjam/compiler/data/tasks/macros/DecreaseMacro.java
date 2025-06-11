package com.wyu4.snowberryjam.compiler.data.tasks.macros;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.VariableReference;
import com.wyu4.snowberryjam.compiler.data.values.math.Minus;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A variable decreaser statement. The name of the variable is stored as
 * {@link SourceKey#NAME}, and the new value is stored as
 * {@link SourceKey#VALUE}.
 */
public class DecreaseMacro implements ExecutableTask {
    /**
     * The name of the variable to decreaser. Can be any type, but the string value
     * will be provided to {@link LocalStorage}.
     * 
     * @see ValueHolder#getString()
     * @see LocalStorage#setVariable(String, Object)
     */
    private final ValueHolder name;

    /**
     * The value to decrease the variable by.
     * 
     * @see LocalStorage#setVariable(String, Object)
     */
    private final ValueHolder value;

    /**
     * Subtraction handler
     */
    private final Minus minusHandler;

    /**
     * Create a new set statement
     * 
     * @param node The {@link JsonNode} to refer
     */
    public DecreaseMacro(JsonNode node) {
        this(
                ValueHolder.fromNode(node.get(SourceKey.NAME.toString())),
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString())));
    }

    /**
     * Create a new set statement
     * 
     * @param name  The name of the variable
     * @param value The value of the variable
     */
    public DecreaseMacro(ValueHolder name, ValueHolder value) {
        this.name = name;
        this.value = value;
        this.minusHandler = new Minus(
                new VariableReference(name),
                value);
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }
        LocalStorage.setVariable(name.getString(), feedback());
    }

    /**
     * @return {@link SourceId#DECREASE_MACRO}
     */
    @Override
    public SourceId getId() {
        return SourceId.DECREASE_MACRO;
    }

    /**
     * @return The new value
     */
    @Override
    public Object feedback() {
        return minusHandler.getValue();
    }

    @Override
    public String toString() {
        return "decrease variable %s by %s".formatted(name, value);
    }
}
