package com.wyu4.snowberryjam.compiler.data.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * Run something in a new thread.
 */
public class ThreadTask implements ExecutableTask {
    /**
     * The body to run
     */
    private final BodyStack body;

    /**
     * Create a new thread
     * @param node The {@link JsonNode} to refer
     */
    public ThreadTask(JsonNode node) {
        this.body = new BodyStack(SourceId.THREAD);
        Compiler.compileBody(node.get(SourceKey.BODY.toString()), body);
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }
        LocalStorage.createThread(body::execute).start();
    }

    /**
     * @return {@link SourceId#THREAD}
     */
    @Override
    public SourceId getId() {
        return SourceId.THREAD;
    }

    /**
     * @return {@code null}
     */
    @Override
    public Object feedback() {
        return null;
    }

    @Override
    public String toString() {
        return "run \"body\" in new thread";
    }
}
