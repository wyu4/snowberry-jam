package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.wyu4.snowberryjam.Compiler.Compiler;
import com.wyu4.snowberryjam.Compiler.DataType.ValidCoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.VariableReference;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

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
}
