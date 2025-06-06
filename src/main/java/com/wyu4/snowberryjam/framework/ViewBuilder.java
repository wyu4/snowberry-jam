package com.wyu4.snowberryjam.framework;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Consumer;

/**
 * The ViewBuilder of the MVCI framework.
 */
public class ViewBuilder implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(ViewBuilder.class);
    private final Model model;
    private final Interactor interactor;

    /**
     * Creates a new ViewBuilder
     * @param model The model of the MVCI framework
     */
    public ViewBuilder(Model model, Interactor interactor) {
        this.model = model;
        this.interactor = interactor;
    }

    @Override
    public Region build() {
        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());
        root.setBottom(createConsole());
        return root;
    }

    public Node createMenuBar() {
        MenuBar bar = new MenuBar();

        Menu fileCategory = new Menu("File");

        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem saveAsFile = new MenuItem("Save As");
        MenuItem exit = new MenuItem("Exit");

        newFile.disableProperty().set(true);
        saveFile.disableProperty().bindBidirectional(model.getSaveDisabledProperty());
        saveAsFile.disableProperty().bindBidirectional(model.getSaveAsDisabledProperty());

        Consumer<File> updateItems = file -> {
            Platform.runLater(() -> {
                model.getSaveDisabledProperty().set(file == null);
                model.getSaveAsDisabledProperty().set(file == null);
            });
        };
        updateItems.accept(model.getSourceFile());
        model.getSourceFileProperty().addListener((evt, old, file) -> {
            updateItems.accept(file);
        });

        openFile.setOnAction(evt -> interactor.createOpenFileTask().run());
        exit.setOnAction(evt -> System.exit(0));

        fileCategory.getItems().addAll(newFile, openFile, saveFile, saveAsFile, new SeparatorMenuItem(), exit);

        bar.getMenus().addAll(fileCategory);

        return bar;
    }

    /**
     * Create a console widget
     * @return {@link Node} Console that displays logs from {@link LocalStorage}
     */
    public Node createConsole() {
        VBox root = new VBox();
        root.setPrefHeight(200);
        root.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
        )));

        HBox topBar = new HBox();

        FontIcon playIcon = new FontIcon(Feather.PLAY);
        Button playButton = new Button();
        playButton.setGraphic(playIcon);

        topBar.getChildren().add(playButton);

        ScrollPane scrollPane = new ScrollPane();

        Consumer<String> printListener = message -> {};
        Consumer<String> warnListener = message -> {};

        root.getChildren().addAll(topBar, scrollPane);

        return root;
    }
}
