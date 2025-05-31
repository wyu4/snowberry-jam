package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import com.wyu4.snowberryjam.Compiler.LocalStorage;

public class PrintTask extends CoreElement implements ExecutableTask {

    private final ValueHolder message;

    public PrintTask(JsonNode node) {
        this(ValueHolder.fromNode(node.get(SourceKey.VALUE.toString())));
    }

    public PrintTask(ValueHolder message) {
        super(SourceId.PRINT);
        this.message = message;
    }

    @Override
    public void execute() {
        LocalStorage.print(feedback());
    }

    @Override
    public Object feedback() {
        return message.getString();
    }

    @Override
    public String toString() {
        return "print \"%s\"".formatted(feedback());
    }
}
