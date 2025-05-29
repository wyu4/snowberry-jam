package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.ValidCoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.VariableReference;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;

public class PrintTask extends ValidCoreElement implements ExecutableTask {
    private String message = "";
    private VariableReference<?> reference;

    public PrintTask(String message) {
        super(SourceId.PRINT);
        this.message = message;
    }

    public PrintTask(VariableReference<?> reference) {
        super(SourceId.PRINT);
        this.reference = reference;
    }

    @Override
    public void execute() {
        Compiler.print(feedback().toString());
    }

    @Override
    public Object feedback() {
        if (reference != null) {
            return reference.getValue();
        }
        return message;
    }

    @Override
    public String toString() {
        String result = "{\"%s\":\"%s\",\"%s\":".formatted(SourceKey.ID, SourceId.PRINT, SourceKey.VALUE);
        if (reference != null) {
            result += "%s}".formatted(reference.toString());
        } else {
            result += "\"%s\"}".formatted(message);
        }
        return result;
    }
}
