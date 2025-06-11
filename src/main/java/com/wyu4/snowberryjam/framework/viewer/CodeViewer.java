package com.wyu4.snowberryjam.framework.viewer;

import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.BodiedTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ElseBodiedTask;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class CodeViewer extends StackPane implements Viewer {
    private final StackPane loadingOverlay = new LoadingOverlay();
    private final ScrollPane scrollPane = new ScrollPane();
    private final StackViewer primaryStackViewer = new StackViewer();

    public CodeViewer() {
        super();

        getStyleClass().add("code-viewer");
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(primaryStackViewer);

        loadingOverlay.setVisible(false);

        getChildren().addAll(scrollPane, loadingOverlay);
    }

    private void flush() {
        primaryStackViewer.flush();
    }

    @Override
    public void refresh() {
        Platform.runLater(() -> {
            loadingOverlay.setVisible(true);
            try {
                flush();
                primaryStackViewer.loadStack(LocalStorage.copyStack());
            } catch (Exception e) {
                Compiler.error("Could not load visual code viewer.", e);
            } finally {
                loadingOverlay.setVisible(false);
            }
        });
    }
}


class LoadingOverlay extends StackPane {
    public LoadingOverlay() {
        super();
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.5), null, null)));
    }
}

class StackViewer extends VBox {
    private AtomicReference<ExecutableTask[]> tasks = new AtomicReference<>();
    private final ObservableList<Node> children;

    public StackViewer() {
        super();
        this.children = getChildren();
    }

    public void loadStack(BodyStack stack) {
        loadStack(stack.copyStack());
    }

    public void loadStack(ExecutableTask[] tasks) {
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

class TaskViewer extends VBox {
    private static final Insets INNER_INSETS = new Insets(0, 5, 0, 10);
    private final ExecutableTask task;
    private final VBox innerContent = new VBox();
    private StackViewer bodyViewer = null;
    private StackViewer secondaryBodyViewer = null;

    

    public TaskViewer(ExecutableTask task) {
        super();
        this.task = task;

        innerContent.setPadding(new Insets(5, 5, 5, 10));

        getChildren().addAll(new Label(task.getId().toString()), innerContent);
    }

    public void load() {
        setBackground(new Background(new BackgroundFill(ColorDictionary.getColorFromId(task.getId()), new CornerRadii(5), Insets.EMPTY)));

        if (task instanceof BodyStack parsed) {
            getBodyViewer().loadStack(parsed);
            return;
        }

        if (task instanceof BodiedTask parsed) {
            getBodyViewer().loadStack(parsed.getBody());
        }

        if (task instanceof ElseBodiedTask parsed) {
            getSecondaryBodyViewer().loadStack(parsed.getSecondaryBody());
        }
    }

    private StackViewer getBodyViewer() {
        if (bodyViewer == null) {
            bodyViewer = new StackViewer();
            bodyViewer.setPadding(INNER_INSETS);
            innerContent.getChildren().add(bodyViewer);
        }
        return bodyViewer;
    }

    private StackViewer getSecondaryBodyViewer() {
        if (secondaryBodyViewer == null) {
            secondaryBodyViewer = new StackViewer();
            secondaryBodyViewer.setPadding(INNER_INSETS);
            innerContent.getChildren().add(secondaryBodyViewer);
        }
        return secondaryBodyViewer;
    }
}

class ValueViewer extends HBox {

}