package com.wyu4.snowberryjam.compiler.data.tasks;

import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * A task that can be added to a {@link com.wyu4.snowberryjam.compiler.data.BodyStack} and executed
 * @see BodyStack#execute()
 * @see BodyStack#addTask(ExecutableTask)
 */
public interface ExecutableTask {
    /**
     * Run the task
     */
    void execute();

    /**
     * Get the ID of the task
     * @return Corresponding {@link SourceId}
     */
    SourceId getId();

    /**
     * Get some feedback from the task. Can be anything, depending on the task.
     * @return Data parsed as {@link Object}
     * @apiNote Some tasks will return null.
     */
    Object feedback();
}
