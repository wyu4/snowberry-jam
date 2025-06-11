package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A variable setter statement. The name of the variable is stored as
 * {@link SourceKey#NAME}, and the new value is stored as
 * {@link SourceKey#VALUE}.
 */
public class SetTask implements ExecutableTask {
    /**
     * The name of the variable to set. Can be any type, but the string value will
     * be provided to {@link LocalStorage}.
     * 
     * @see ValueHolder#getString()
     * @see LocalStorage#setVariable(String, Object)
     */
    private final ValueHolder name;
    /**
     * The new value of the variable.
     * 
     * @see LocalStorage#setVariable(String, Object)
     */
    private final ValueHolder value;

    /**
     * Create a new set statement
     * 
     * @param node The {@link JsonNode} to refer
     */
    public SetTask(JsonNode node) {
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
    public SetTask(ValueHolder name, ValueHolder value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }

        LocalStorage.setVariable(name.getString(), feedback());
    }

    /**
     * @return {@link SourceId#SET}
     */
    @Override
    public SourceId getId() {
        return SourceId.SET;
    }

    protected ValueHolder getNameValue() {
        return name;
    }

    /**
     * @return The new value
     */
    @Override
    public Object feedback() {
        return value.getValue();
    }

    @Override
    public String toString() {
        return "set variable %s to %s".formatted(name, value);
    }
}
