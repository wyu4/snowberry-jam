package com.wyu4.snowberryjam.framework;

import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
     * 
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
     * 
     * @return {@link Node} Console that displays logs from {@link LocalStorage}
     */
    public Node createConsole() {
        final AtomicBoolean atBottom = new AtomicBoolean(true);

        VBox root = new VBox();
        root.getStyleClass().add("console");
        root.setPrefHeight(250);
        root.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        HBox topBar = new HBox();

        FontIcon playIcon = new FontIcon(Feather.PLAY);
        Button playButton = new Button();
        playButton.setGraphic(playIcon);
        playButton.setOnAction(evt -> LocalStorage.runStack());

        topBar.getChildren().add(playButton);

        ScrollPane scrollPane = new ScrollPane();
        VBox logs = new VBox();
        logs.setAlignment(Pos.BOTTOM_CENTER);
        scrollPane.setContent(logs);

        Consumer<Double> setAtBottom = height -> {
            atBottom.set(height >= 1.0);
        };

        scrollPane.vvalueProperty().addListener((evt, old, height) -> setAtBottom.accept(height.doubleValue()));

        Consumer<Node> addLog = log -> {
            model.getLogNumberProperty().set(model.getLogNumber() + 1);
            Platform.runLater(() -> {
                ObservableList<Node> children = logs.getChildren();
                    if (children.size() >= 100) {
                        children.removeFirst();
                    }
                if (atBottom.get()) {
                    children.add(log);
                    new Thread(() -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            LocalStorage.error(e);
                        }
                        scrollPane.vvalueProperty().set(1.0);
                    }).start();
                } else {
                    logs.getChildren().add(log);
                }
            });
        };

        LocalStorage.addPrintListener(message -> {
            addLog.accept(createLog("LOCAL", message, Color.rgb(0, 0, 0, 0)));
        });
        LocalStorage.addWarnListener(message -> {
            addLog.accept(createLog("LOCAL", message, Color.LIGHTYELLOW));
        });
        Compiler.addErrorListener(message -> {
            addLog.accept(createLog("LOCAL", message, Color.ORANGE));
        });

        Compiler.addPrintListener(message -> {
            addLog.accept(createLog("COMPILER", message, Color.rgb(0, 0, 0, 0)));
        });
        Compiler.addWarnListener(message -> {
            addLog.accept(createLog("COMPILER", message, Color.YELLOW));
        });
        Compiler.addErrorListener(message -> {
            addLog.accept(createLog("COMPILER", message, Color.RED));
        });

        root.getChildren().addAll(topBar, scrollPane);

        return root;
    }

    public Node createLog(String source, String message, Color color) {
        HBox root = new HBox();
        root.setPadding(new Insets(0, 20, 0, 20));
        root.setSpacing(10);
        root.setBackground(new Background(new BackgroundFill(color, null, null)));

        Label sourceLabel = new Label("[%s] [%s]".formatted(model.getLogNumber(), source));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        root.getChildren().addAll(sourceLabel, messageLabel);

        return root;
    }
}
