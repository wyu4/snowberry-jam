package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.DataType.Tasks.ExecutableTask;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.LocalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BodyStack extends CoreElement implements ExecutableTask {
    private static final Logger logger = LoggerFactory.getLogger("Stack Debugger");
    private static final AtomicBoolean DEBUGGING_ENABLED = new AtomicBoolean(false);
    private final List<ExecutableTask> stack = new ArrayList<>();

    public static void setDebuggingEnabled(boolean state) {
        DEBUGGING_ENABLED.set(state);
    }

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
            if (DEBUGGING_ENABLED.get()) {
                logger.debug(element.toString());
            }
            element.execute();
        }
    }
}
