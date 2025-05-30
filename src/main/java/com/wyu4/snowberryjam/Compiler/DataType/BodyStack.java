package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.DataType.Tasks.ExecutableTask;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

import java.util.ArrayList;
import java.util.List;

public class BodyStack extends CoreElement implements ExecutableTask {
    private final List<ExecutableTask> stack = new ArrayList<>();

    public BodyStack(SourceId id) {
        super(id);
    }

    public void addElement(ExecutableTask element) {
        if (element == null) {
            return;
        }
        stack.add(element);
    }

    public ExecutableTask[] copyStack() {
        ExecutableTask[] array = new ExecutableTask[stack.size()];
        stack.toArray(array);
        return array;
    }

    public void flush() {
        stack.clear();
    }

    @Override
    public void execute() {
        for (ExecutableTask element : copyStack()) {
            element.execute();
        }
    }
}
