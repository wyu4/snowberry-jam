package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wait task. The time to wait is stored as {@link SourceKey#VALUE}.
 */
public class WaitTask implements ExecutableTask {
    private static final Logger logger = LoggerFactory.getLogger("WaitTask");
    private static final int INTERVAL = 10;

    /**
     * The wait time in seconds.
     */
    private final ValueHolder time;

    /**
     * Create a new wait statement
     * 
     * @param node The {@link JsonNode} to refer
     */
    public WaitTask(JsonNode node) {
        time = ValueHolder.fromNode(node.get(SourceKey.VALUE.toString()));
    }

    @Override
    public void execute() {
        double milliseconds = getTime();
        long startTime = System.currentTimeMillis();
        while (LocalStorage.isRunning() && ((System.currentTimeMillis() - startTime) < milliseconds)) {
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * @return {@link SourceId#WAIT}
     */
    @Override
    public SourceId getId() {
        return SourceId.WAIT;
    }

    /**
     * @return The time to wait parsed as an {@link Object}
     * @see #getTime()
     */
    @Override
    public Object feedback() {
        return getTime();
    }

    /**
     * @return The time to wait as a {@link Long}
     */
    private long getTime() {
        Double size = time.getSize();
        if (size == null) {
            return 0;
        }
        return Math.round(size * 1000);
    }

    @Override
    public String toString() {
        return "wait %s seconds".formatted(feedback());
    }
}
