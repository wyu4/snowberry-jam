package com.wyu4.snowberryjam.gui.viewer.codeviewer.tasks;

import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.BodiedTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ElseBodiedTask;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TaskViewer extends VBox {
    private static final CornerRadii corner = new CornerRadii(5);
    
    private final ExecutableTask task;
    private final HBox titleHBox = new HBox(5);
    private VBox innerContent = null;
    private StackViewer bodyViewer = null;
    private StackViewer secondaryBodyViewer = null;

    public TaskViewer(ExecutableTask task) {
        super();
        this.task = task;
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxWidth(Region.USE_PREF_SIZE);
        setBorder(new Border(new BorderStroke(Color.rgb(54, 52, 53), BorderStrokeStyle.SOLID, corner, BorderWidths.DEFAULT)));
        setPadding(new Insets(5, 10, 5, 10));
        setSpacing(5);

        Label title = new Label(task.getId().getBeautified());
        titleHBox.getChildren().add(title);

        getChildren().addAll(titleHBox);
    }

    public void load() {
        setBackground(new Background(
                new BackgroundFill(ColorDictionary.getColorFromId(task.getId()), corner, Insets.EMPTY)));

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

    private VBox getInnerContent() {
        if (innerContent == null) {
            innerContent = new VBox(10);
            getChildren().add(innerContent);
        }
        return innerContent;
    }

    private void addValueProperty() {
        
    }

    private StackViewer getBodyViewer() {
        if (bodyViewer == null) {
            bodyViewer = new StackViewer();
            getInnerContent().getChildren().add(bodyViewer);
        }
        return bodyViewer;
    }

    private StackViewer getSecondaryBodyViewer() {
        if (secondaryBodyViewer == null) {
            VBox box = new VBox();
            secondaryBodyViewer = new StackViewer();
            box.getChildren().addAll(new Label("else: "), secondaryBodyViewer);
            getInnerContent().getChildren().add(box);
        }
        return secondaryBodyViewer;
    }
}