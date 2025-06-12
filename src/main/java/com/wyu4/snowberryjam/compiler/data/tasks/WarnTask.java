package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ValuedTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A warn statement. The message is stored as {@link SourceKey#VALUE}. Unless there's something wrong with the {@link ValueHolder} or {@link LocalStorage} classes, this class is guaranteed to never throw an error.
 */
public class WarnTask implements ExecutableTask, ValuedTask {
    /**
     * The message. Can be any type.
     */
    private final ValueHolder message;

    /**
     * Create a new warn statement.
     * @param node The {@link JsonNode} to refer
     */
    public WarnTask(JsonNode node) {
        this(ValueHolder.fromNode(node.get(SourceKey.VALUE.toString())));
    }

    /**
     * Create a new warn statement.
     * @param message the message to warn
     */
    public WarnTask(String message) {
        this(new ValueHolder(message));
    }

    /**
     * Create a new warn statement.
     * @param message {@link ValueHolder} pointing to the message to warn
     */
    public WarnTask(ValueHolder message) {
        this.message = message;
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }
        
        LocalStorage.warn(feedback());
    }

    /**
     * @return {@link SourceId#WARN}
     */
    @Override
    public SourceId getId() {
        return SourceId.WARN;
    }

    /**
     * @return The message as a string.
     */
    @Override
    public Object feedback() {
        return message.getString();
    }

    @Override
    public String toString() {
        return "warn %s".formatted(message);
    }

    @Override
    public ValueHolder getValue() {
        return message;
    }
}
