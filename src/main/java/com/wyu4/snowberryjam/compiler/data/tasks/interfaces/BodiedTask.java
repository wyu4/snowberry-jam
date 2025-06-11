package com.wyu4.snowberryjam.compiler.data.tasks.interfaces;

import com.wyu4.snowberryjam.compiler.data.BodyStack;

/**
 * A task with a body.
 */
public interface BodiedTask {
    /**
     * Get the stack of this task
     * @return {@link BodyStack} of this task
     */
    BodyStack getBody();
}
