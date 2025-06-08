package com.wyu4.snowberryjam.framework;

import com.wyu4.snowberryjam.codeutils.AutoComplete;
import com.wyu4.snowberryjam.codeutils.Filter;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
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

        SplitPane body = new SplitPane(createCodeEditor(), createConsole());
        body.setOrientation(Orientation.VERTICAL);
        root.setCenter(body);

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

        Menu editCategory = new Menu("Edit");
        MenuItem formatDocument = new MenuItem("Format Document");


        newFile.disableProperty().set(true);
        saveFile.disableProperty().bindBidirectional(model.getSaveDisabledProperty());
        saveAsFile.disableProperty().bindBidirectional(model.getSaveAsDisabledProperty());

        openFile.setOnAction(evt -> interactor.createOpenFileTask().run());
        exit.setOnAction(evt -> System.exit(0));

        formatDocument.setOnAction(evt -> interactor.createFormatCodeTask().run());

        fileCategory.getItems().addAll(newFile, openFile, saveFile, saveAsFile, new SeparatorMenuItem(), exit);
        editCategory.getItems().addAll(formatDocument);

        bar.getMenus().addAll(fileCategory, editCategory);

        return bar;
    }

    /**
     * Create a console widget
     * 
     * @return {@link Node} Console that displays logs from {@link LocalStorage}
     */
    public Node createConsole() {
        final AtomicBoolean atBottom = new AtomicBoolean(true);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("console");
        root.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(1, 0, 0, 0))));

        HBox topBar = new HBox();
        topBar.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.1), null, null)));

        FontIcon playIcon = new FontIcon(Feather.PLAY);
        Button playButton = new Button();
        playButton.setGraphic(playIcon);
        playButton.setOnAction(evt -> interactor.createRunTask().run());

        topBar.getChildren().add(playButton);

        ScrollPane scrollPane = new ScrollPane();
        VBox logs = new VBox();
        logs.setAlignment(Pos.BOTTOM_CENTER);
        scrollPane.setContent(logs);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.viewportBoundsProperty().addListener((evt, old, bounds) -> {
            logs.setPrefWidth(bounds.getWidth());
        });

        Consumer<Double> setAtBottom = height -> {
            atBottom.set(height >= 1.0);
        };

        scrollPane.vvalueProperty().addListener((evt, old, height) -> setAtBottom.accept(height.doubleValue()));

        Consumer<Node> addLog = log -> {
            model.getLogNumberProperty().set(model.getLogNumber() + 1);
            Platform.runLater(() -> {
                ObservableList<Node> children = logs.getChildren();
                    if (children.size() >= 1000) {
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

        LocalStorage.addPrintListener((name, message) -> addLog.accept(createLog(name, message, Color.rgb(0, 0, 0, 0))));
        LocalStorage.addWarnListener((name, message) -> addLog.accept(createLog(name, message, Color.LIGHTYELLOW)));
        LocalStorage.addErrorListener((name, message) -> addLog.accept(createLog(name, message, Color.MEDIUMVIOLETRED)));

        Compiler.addPrintListener((name, message) -> addLog.accept(createLog(name, message, Color.rgb(0, 0, 0, 0))));
        Compiler.addWarnListener((name, message) -> addLog.accept(createLog(name, message, Color.YELLOW)));
        Compiler.addErrorListener((name, message) -> addLog.accept(createLog(name, message, Color.RED)));

        root.setTop(topBar);
        root.setCenter(scrollPane);

        return root;
    }

    public Node createLog(String source, String message, Color color) {
        HBox root = new HBox();
        root.setPadding(new Insets(0, 20, 0, 20));
        root.setSpacing(10);
        root.setBackground(new Background(new BackgroundFill(color, null, null)));

        Label sourceLabel = new Label("[%s]".formatted(source));
        sourceLabel.setMinSize(200, sourceLabel.getPrefHeight());
        sourceLabel.setMaxSize(sourceLabel.getPrefWidth(), sourceLabel.getPrefHeight());
        HBox.setHgrow(sourceLabel, Priority.ALWAYS);

        Label messageArea = new Label(message);
        messageArea.setText(message);
        messageArea.setWrapText(true);
        
        HBox.setHgrow(messageArea, Priority.NEVER);

        root.getChildren().addAll(sourceLabel, messageArea);

        return root;
    }

    public Node createCodeEditor() {
        CodeArea area = new CodeArea(LocalStorage.getDefaultSource());
        area.getStyleClass().add("editor");
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));

        area.textProperty().addListener((evt, old, source) -> {
            if (!source.equals(model.getSourceCode())) {
                model.getSourceCodeProperty().set(source);
            }
        });

        area.setOnKeyPressed(event -> {
            if (Filter.isSpecialOperation(event)) {
                return;
            };

            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.TAB)) {
                AutoComplete.formatIndent(area, event);
            }
        });

        area.setOnKeyReleased(event -> {
            if (Filter.isSpecialOperation(event)) {
                return;
            };

            KeyCode keyCode = event.getCode();
            if (
                !keyCode.equals(KeyCode.SHIFT) &&
                !keyCode.equals(KeyCode.ALT) &&
                !keyCode.equals(KeyCode.CONTROL) &&
                !keyCode.equals(KeyCode.TAB) &&
                !keyCode.equals(KeyCode.CAPS) &&
                !keyCode.equals(KeyCode.BACK_SPACE) &&
                !keyCode.isArrowKey()
            ) {
                AutoComplete.fullfillPunctation(area);
            }
        });

        area.setOnKeyPressed(event -> {
            if (Filter.isSpecialOperation(event)) {
                return;
            };
            
            KeyCode keyCode = event.getCode();

            if (keyCode.equals(KeyCode.ENTER)) {
                AutoComplete.autoIndent(area);
                return;
            }
        });

        area.replaceText(model.getSourceCode());
        model.getSourceCodeProperty().addListener((evt, old, source) -> {
            if (!source.equals(area.getText())) {
                Platform.runLater(() -> area.replaceText(source));
            }
        });

        return new VirtualizedScrollPane<>(area);
    }
}
