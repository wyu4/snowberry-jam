package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.DataType.Tasks.ExecutableTask;
import com.wyu4.snowberryjam.Compiler.Enums.SourceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The stack that stores Snowberry Jam instructions
 */
public class BodyStack implements ExecutableTask {
    private static final Logger logger = LoggerFactory.getLogger("Stack Debugger");
    private static final AtomicBoolean DEBUGGING_ENABLED = new AtomicBoolean(false);
    private final SourceId id;
    private final List<ExecutableTask> stack = new ArrayList<>();

    public static void setDebuggingEnabled(boolean state) {
        DEBUGGING_ENABLED.set(state);
    }

    /**
     * Create a new Stack object
     * @param id The ID of the stack (doesn't affect how the stack executes its tasks.)
     */
    public BodyStack(SourceId id) {
        this.id = id;
    }

    /**
     * Add an instruction into the stack
     * @param element Element containing an instruction
     */
    public void addTask(ExecutableTask element) {
        if (element == null) {
            return;
        }
        stack.add(element);
    }

    /**
     * Get a deep copy of the stack
     * @return An array of {@link ExecutableTask}
     * @see ExecutableTask
     */
    public ExecutableTask[] copyStack() {
        ExecutableTask[] array = new ExecutableTask[stack.size()];
        stack.toArray(array);
        return array;
    }

    /**
     * Flush the contents of the stack
     */
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

    @Override
    public Object feedback() {
        return copyStack();
    }

    @Override
    public SourceId getId() {
        return id;
    }
}
