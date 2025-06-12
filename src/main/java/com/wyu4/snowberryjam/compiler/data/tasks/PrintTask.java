package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ValuedTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A print statement. The message is stored as {@link SourceKey#VALUE}. Unless there's something wrong with the {@link ValueHolder} or {@link LocalStorage} classes, this class is guaranteed to never throw an error.
 */
public class PrintTask implements ExecutableTask, ValuedTask {
    /**
     * The message. Can be any type.
     */
    private final ValueHolder message;

    /**
     * Create a new print statement.
     * @param node The {@link JsonNode} to refer
     */
    public PrintTask(JsonNode node) {
        this(ValueHolder.fromNode(node.get(SourceKey.VALUE.toString())));
    }

    /**
     * Create a new print statement.
     * @param message the message to print
     */
    public PrintTask(String message) {
        this(new ValueHolder(message));
    }

    /**
     * Create a new print statement.
     * @param message {@link ValueHolder} pointing to the message to print
     */
    public PrintTask(ValueHolder message) {
        this.message = message;
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }
        
        LocalStorage.print(feedback());
    }

    /**
     * @return {@link SourceId#PRINT}
     */
    @Override
    public SourceId getId() {
        return SourceId.PRINT;
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
        return "print %s".formatted(message);
    }

    @Override
    public ValueHolder getValue() {
        return message;
    }
}
