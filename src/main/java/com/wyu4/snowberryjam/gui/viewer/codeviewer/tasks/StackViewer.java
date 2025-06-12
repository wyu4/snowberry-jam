package com.wyu4.snowberryjam.gui.viewer.codeviewer.tasks;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StackViewer extends VBox {
    private AtomicReference<ExecutableTask[]> tasks = new AtomicReference<>();
    private final ObservableList<Node> children;

    public StackViewer() {
        this(false);
    }

    public StackViewer(boolean silent) {
        super();
        this.children = getChildren();
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxWidth(Region.USE_PREF_SIZE);
        if (!silent) {
            setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.1), new CornerRadii(5), Insets.EMPTY)));
        }
    }

    public void loadStack(BodyStack stack) {
        loadStack(stack.copyStack());
    }

    public void loadStack(ExecutableTask[] tasks) {
        children.clear();
        ExecutableTask[] previous = this.tasks.get();
        if (previous != null && Arrays.equals(previous, tasks)) {
            return;
        }
        this.tasks.set(tasks);

        for (ExecutableTask currentTask : tasks) {
            TaskViewer viewer = new TaskViewer(currentTask);
            children.add(viewer);
            viewer.load();
        }
    }

    public void flush() {
        this.tasks.set(null);
        children.clear();
    }
}
