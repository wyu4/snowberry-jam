package com.wyu4.snowberryjam.gui.viewer.codeviewer;

import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.gui.viewer.Viewer;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.tasks.StackViewer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.atomic.AtomicReference;

public class CodeViewer extends StackPane implements Viewer {
    private final StackPane loadingOverlay = new LoadingOverlay();
    private final ScrollPane scrollPane = new ScrollPane();
    private final StackViewer primaryStackViewer = new StackViewer(true);

    public CodeViewer() {
        super();

        getStyleClass().add("code-viewer");

        StackPane background = new DottedStackPane(20);
        background.setAlignment(Pos.TOP_LEFT);
        background.setPadding(new Insets(10, 10, 10, 10));
        background.getChildren().add(primaryStackViewer);
        background.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds scrollViewport = scrollPane.getViewportBounds();
            return Math.max(
                    scrollViewport != null ? scrollViewport.getWidth() : 0,
                    primaryStackViewer.getBoundsInParent().getMaxX());
        }, scrollPane.viewportBoundsProperty(), primaryStackViewer.boundsInParentProperty()));
        background.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds scrollViewport = scrollPane.getViewportBounds();
            return Math.max(
                    scrollViewport != null ? scrollViewport.getHeight() : 0,
                    primaryStackViewer.getBoundsInParent().getMaxY());
        }, scrollPane.viewportBoundsProperty(), primaryStackViewer.boundsInParentProperty()));

        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(background);

        loadingOverlay.setVisible(false);

        getChildren().addAll(scrollPane, loadingOverlay);

        final AtomicReference<Double> initialMouseX = new AtomicReference<>(0.0);
        final AtomicReference<Double> initialMouseY = new AtomicReference<>(0.0);
        final AtomicReference<Double> initialX = new AtomicReference<>(0.0);
        final AtomicReference<Double> initialY = new AtomicReference<>(0.0);
        background.setOnMousePressed(event -> {
            initialX.set(scrollPane.getHvalue());
            initialY.set(scrollPane.getVvalue());
            initialMouseX.set(event.getSceneX());
            initialMouseY.set(event.getSceneY());
        });
        background.setOnMouseDragged(event -> {
            Bounds viewport = scrollPane.getViewportBounds();
            Bounds content = primaryStackViewer.getBoundsInLocal();

            double hDelta = (event.getSceneX() - initialMouseX.get()) / (content.getWidth() - viewport.getWidth());
            double vDelta = (event.getSceneY() - initialMouseY.get()) / (content.getHeight() - viewport.getHeight());

            Platform.runLater(() -> {
                scrollPane.setHvalue(Math.clamp(initialX.get() - hDelta, 0, 1));
                scrollPane.setVvalue(Math.clamp(initialY.get() - vDelta, 0, 1));
            });
        });
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

class DottedStackPane extends StackPane {
    public DottedStackPane(int spacing) {
        super();
        Circle dot = new Circle(2, Color.rgb(0, 0, 0, 0.1));
        dot.setTranslateX(spacing/2);
        dot.setTranslateY(spacing/2);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        WritableImage image = new WritableImage(spacing, spacing);
        dot.snapshot(params, image);

        setBackground(new Background(new BackgroundImage(
                image,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(spacing, spacing, false, false, false, false)
        )));
    }
}

class LoadingOverlay extends StackPane {
    public LoadingOverlay() {
        super();
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.5), null, null)));
    }
}