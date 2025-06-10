package com.wyu4.snowberryjam.framework.viewer;

import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;
import javafx.scene.layout.StackPane;

import java.util.concurrent.atomic.AtomicReference;

public class StackViewer extends StackPane {
    private AtomicReference<ExecutableTask[]> tasks = new AtomicReference<>();

    public StackViewer() {
        super();
    }

    public StackViewer(BodyStack stack) {
        super();
        setStack(stack);
    }

    public void setStack(BodyStack stack) {
        this.tasks.set(stack.copyStack());
    }

    public void setStack(ExecutableTask[] tasks) {
        this.tasks.set(tasks);
    }

    
}
