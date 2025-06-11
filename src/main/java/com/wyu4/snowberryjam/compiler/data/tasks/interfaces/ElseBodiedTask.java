package com.wyu4.snowberryjam.compiler.data.tasks.interfaces;

import com.wyu4.snowberryjam.compiler.data.BodyStack;

/**
 * A task with a second body.
 */
public interface ElseBodiedTask {
    /**
     * Get the second stack of this task
     * @return Second {@link BodyStack} of this task
     */
    BodyStack getSecondaryBody();
}
