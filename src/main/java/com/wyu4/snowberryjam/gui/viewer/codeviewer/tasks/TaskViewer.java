package com.wyu4.snowberryjam.gui.viewer.codeviewer.tasks;

import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;
import com.wyu4.snowberryjam.compiler.data.tasks.SetTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.BodiedTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ElseBodiedTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.NamedTask;
import com.wyu4.snowberryjam.compiler.data.tasks.interfaces.ValuedTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.values.ValueViewer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    private final SourceId id;
    private final HBox titleHBox = new HBox(5);
    private HBox bottomHBox = null;
    private Node nameProperty = null;
    private Node valueProperty = null;
    private VBox innerContent = null;
    private StackViewer bodyViewer = null;
    private StackViewer secondaryBodyViewer = null;

    public TaskViewer(ExecutableTask task) {
        super();
        this.task = task;
        this.id = task.getId();
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxWidth(Region.USE_PREF_SIZE);
        setBorder(new Border(new BorderStroke(Color.rgb(54, 52, 53), BorderStrokeStyle.SOLID, corner, BorderWidths.DEFAULT)));
        setPadding(new Insets(5, 10, 5, 10));
        setSpacing(5);

        Label title = new Label(task.getId().getBeautified());
        titleHBox.getChildren().add(title);
        titleHBox.setAlignment(Pos.CENTER_LEFT);

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

        if (task instanceof NamedTask parsed) {
            addNameProperty(parsed.getName());
        }

        if (task instanceof ValuedTask parsed) {
            addValueProperty(parsed.getValue());
        }
    }

    private VBox getInnerContent() {
        if (innerContent == null) {
            innerContent = new VBox(10);
            getChildren().add(innerContent);
        }
        return innerContent;
    }

    private HBox getBottomHBox() {
        if (bottomHBox == null) {
            bottomHBox = new HBox(5);
            bottomHBox.setAlignment(Pos.CENTER_LEFT);
            getInnerContent().getChildren().add(bottomHBox);
        }
        return bottomHBox;
    }

    private void addNameProperty(ValueHolder name) {
        if (nameProperty != null) {
            return;
        }
        nameProperty = ValueViewer.buildValueViewer(name);

        if (SourceId.REPEAT.equals(task.getId())) {
            if (name.notEmpty()) {
                getBottomHBox().getChildren().addAll(new Label("and set variable"), nameProperty);
            }
            return;
        }

        titleHBox.getChildren().add(nameProperty);
        if (SourceId.SET.equals(task.getId())) {
            titleHBox.getChildren().add(new Label("to"));
        } 
    }

    private void addValueProperty(ValueHolder value) {
        if (valueProperty != null) {
            return;
        }

        valueProperty = ValueViewer.buildValueViewer(value);
        titleHBox.getChildren().add(valueProperty);

        if (SourceId.WAIT.equals(task.getId())) {
            titleHBox.getChildren().add(new Label("seconds"));
        } 
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