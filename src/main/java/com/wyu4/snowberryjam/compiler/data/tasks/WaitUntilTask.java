package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wait task. The condition to wait for {@link SourceKey#VALUE}.
 */
public class WaitUntilTask implements ExecutableTask {
    private static final Logger logger = LoggerFactory.getLogger("WaitTask");
    private static final int INTERVAL = 10;

    /**
     * The condition to wait for.
     */
    private final ValueHolder condition;

    /**
     * Create a new wait statement
     * 
     * @param node The {@link JsonNode} to refer
     */
    public WaitUntilTask(JsonNode node) {
        condition = ValueHolder.checkValueIsConditional(
                ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()))
        );
    }

    @Override
    public void execute() {
        while (LocalStorage.isRunning() && feedback().equals(false)) {
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * @return {@link SourceId#WAIT_UNTIL}
     */
    @Override
    public SourceId getId() {
        return SourceId.WAIT_UNTIL;
    }

    /**
     * @return The state of the condition
     */
    @Override
    public Object feedback() {
        return condition.getValue();
    }

    @Override
    public String toString() {
        return "wait until %s".formatted(condition);
    }
}
