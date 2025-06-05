package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Enums.SourceId;
import com.wyu4.snowberryjam.Compiler.Enums.SourceKey;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

/**
 * A print statement. The message is stored as {@link SourceKey#VALUE}. Unless there's something wrong with the {@link ValueHolder} or {@link LocalStorage} classes, this class is guaranteed to never throw an error.
 */
public class PrintTask implements ExecutableTask {
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
     * @param message {@link ValueHolder} pointing to the message to print
     */
    public PrintTask(ValueHolder message) {
        this.message = message;
    }

    @Override
    public void execute() {
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
}
