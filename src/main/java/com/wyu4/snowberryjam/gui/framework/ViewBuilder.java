package com.wyu4.snowberryjam.gui.framework;

import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.gui.editorutils.AutoComplete;
import com.wyu4.snowberryjam.gui.editorutils.Filter;
import com.wyu4.snowberryjam.gui.framework.Model.Page;
import com.wyu4.snowberryjam.gui.viewer.VariableViewer;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.CodeViewer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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

        SplitPane body = new SplitPane(createProjectWidget(), createConsole());
        body.setOrientation(Orientation.VERTICAL);
        body.setDividerPositions(1);
        root.setCenter(body);

        return root;
    }

    /**
     * Create a new {@link MenuBar} filled with project operations.
     * @return {@link MenuBar}
     */
    public Node createMenuBar() {
        MenuBar bar = new MenuBar();

        Menu fileCategory = new Menu("File");

        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem saveAsFile = new MenuItem("Save As");
        MenuItem exit = new MenuItem("Exit");

        saveFile.disableProperty().bindBidirectional(model.getSaveDisabledProperty());
        saveAsFile.disableProperty().bindBidirectional(model.getSaveAsDisabledProperty());

        newFile.setOnAction(evt -> interactor.createNewProjectTask().run());
        openFile.setOnAction(evt -> interactor.createOpenFileTask().run());
        saveFile.setOnAction(evt -> interactor.createSaveFileTask().run());
        saveAsFile.setOnAction(evt -> interactor.createSaveAsFileTask().run());
        exit.setOnAction(evt -> Platform.exit());

        fileCategory.getItems().addAll(newFile, openFile, saveFile, saveAsFile, new SeparatorMenuItem(), exit);

        Menu documentCategory = new Menu("Document");
        MenuItem formatDocument = new MenuItem("Format");
        MenuItem compileDocument = new MenuItem("Compile");
        MenuItem runDocument = new MenuItem("Run");
        MenuItem stopDocument = new MenuItem("Stop");

        compileDocument.disableProperty().bind(model.getCompilingProperty());
        runDocument.disableProperty().bind(model.getRunningProperty().and(model.getRunDisabledProperty()));
        stopDocument.disableProperty().bind(model.getRunningProperty().not());

        formatDocument.setOnAction(evt -> interactor.createFormatCodeTask().run());
        compileDocument.setOnAction(evt -> interactor.createCompileTask().run());
        runDocument.setOnAction(evt -> interactor.createRunTask().run());
        stopDocument.setOnAction(evt -> interactor.createStopTask().run());
        
        documentCategory.getItems().addAll(formatDocument, compileDocument, runDocument, stopDocument);

        bar.getMenus().addAll(fileCategory, documentCategory);

        return bar;
    }

    /**
     * Create a project widget
     * @return A widget with a code editor, viewer, and console.
     * @see #createConsole()
     * @see #createCodeEditor()
     * @see #createProjectViewer()
     */
    public Node createProjectWidget() {
        BorderPane root = new BorderPane();

        Node codeArea = createCodeEditor();
        Node codeViewer = createProjectViewer();

        Consumer<Page> updatepage = page -> {
            Platform.runLater(() -> {
                codeArea.setVisible(Page.EDITOR.equals(page));
                codeViewer.setVisible(Page.VIEWER.equals(page));
            });
        };
        updatepage.accept(model.getPage());
        model.getPageProperty().addListener((evt, old, page) -> updatepage.accept(page));

        HBox modeBar = new HBox(10);
        modeBar.setPadding(new Insets(2, 10, 2, 10));
        modeBar.setAlignment(Pos.CENTER);

        Button editorButton = new Button();
        Button viewerButton = new Button();

        editorButton.setGraphic(new FontIcon(Feather.FILE_TEXT));
        viewerButton.setGraphic(new FontIcon(Feather.EYE));

        editorButton.setOnAction(evt -> model.getPageProperty().set(Page.EDITOR));
        viewerButton.setOnAction(evt -> model.getPageProperty().set(Page.VIEWER));

        modeBar.getChildren().addAll(editorButton, viewerButton);

        StackPane center = new StackPane();
        center.getChildren().addAll(codeArea, codeViewer);

        root.setCenter(center);
        root.setBottom(modeBar);
        return root;
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
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT,
                new Insets(1, 0, 0, 0))));

        HBox topBar = new HBox();
        topBar.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.1), null, null)));
        topBar.setPadding(new Insets(2, 10, 2, 10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        final FontIcon playIcon = new FontIcon(Feather.PLAY);
        final FontIcon stopIcon = new FontIcon(Feather.SQUARE);
        Button playButton = new Button();
        playButton.graphicProperty().bind(Bindings.when(model.getRunningProperty()).then(stopIcon).otherwise(playIcon));
        playButton.setOnAction(evt -> {
            if (model.getRunning()) {
                interactor.createStopTask().run();
            } else {
                interactor.createRunTask().run();
            }
        });
        playButton.disableProperty().bind(model.getRunDisabledProperty());

        Button clearButton = new Button();
        clearButton.setGraphic(new FontIcon(Feather.TRASH));

        topBar.getChildren().addAll(playButton, clearButton);

        ScrollPane scrollPane = new ScrollPane();
        VBox logs = new VBox();
        logs.setAlignment(Pos.BOTTOM_CENTER);
        scrollPane.setContent(logs);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.viewportBoundsProperty().addListener((evt, old, bounds) -> {
            logs.setPrefWidth(bounds.getWidth());
        });

        Consumer<Double> setAtBottom = height -> {
            atBottom.set(height >= 1.0);
        };

        scrollPane.vvalueProperty().addListener((evt, old, height) -> setAtBottom.accept(height.doubleValue()));

        ObservableList<Node> logsChildren = logs.getChildren();
        Consumer<Node> addLog = log -> Platform.runLater(() -> {
            if (logsChildren.size() >= 1000) {
                logsChildren.removeFirst();
            }
            logsChildren.add(log);
            if (atBottom.get()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        LocalStorage.error(e);
                    }
                    scrollPane.vvalueProperty().set(1.0);
                }).start();
            }
        });
        clearButton.setOnAction(evt -> Platform.runLater(logsChildren::clear));

        LocalStorage
                .addPrintListener((name, message) -> addLog.accept(createLog(name, message, Color.rgb(0, 0, 0, 0))));
        LocalStorage.addWarnListener((name, message) -> addLog.accept(createLog(name, message, Color.LIGHTYELLOW)));
        LocalStorage
                .addErrorListener((name, message) -> addLog.accept(createLog(name, message, Color.MEDIUMVIOLETRED)));

        Compiler.addPrintListener((name, message) -> addLog.accept(createLog(name, message, Color.rgb(0, 0, 0, 0))));
        Compiler.addWarnListener((name, message) -> addLog.accept(createLog(name, message, Color.YELLOW)));
        Compiler.addErrorListener((name, message) -> addLog.accept(createLog(name, message, Color.RED)));

        HBox inputBox = new HBox(10);
        inputBox.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0), null, null)));
        inputBox.setPadding(new Insets(10, 10, 10, 10));
        inputBox.setAlignment(Pos.CENTER);

        final StringProperty inputProperty = new SimpleStringProperty("");
        final AtomicBoolean sendingInput = new AtomicBoolean(false);
        final Runnable sendInput = () -> {
            if (sendingInput.get()) {
                return;
            }

            sendingInput.set(true);
            try {
                Platform.runLater(() -> {
                    String input = inputProperty.get();
                    inputProperty.set("");
                    LocalStorage.sendInput(input);
                });
            } catch (Exception e) {
                LocalStorage.error("Could not send input.", e);
            } finally {
                sendingInput.set(false);
            }
        };

        TextField inputField = new TextField();
        inputField.textProperty().bindBidirectional(inputProperty);
        inputField.setOnKeyPressed(event -> {
            if (KeyCode.ENTER.equals(event.getCode())) {
                sendInput.run();
            }
        });

        Button inputButton = new Button();
        inputButton.setGraphic(new FontIcon(Feather.SEND));
        inputButton.setOnAction(evt -> sendInput.run());

        inputField.prefWidthProperty().bind(inputBox.widthProperty().subtract(inputButton.widthProperty()));

        inputBox.getChildren().addAll(inputField, inputButton);

        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setBottom(inputBox);

        return root;
    }

    /**
     * Create a log widget to be inserted in the console
     * @param source The source of the log
     * @param message The message
     * @param color The background color
     * @return A new {@link HBox}
     */
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

    /**
     * Creates a debug viewer for the compiled code.
     * @return A new {@link SplitPane}
     */
    public Node createProjectViewer() {
        final CodeViewer codeViewer = new CodeViewer();
        final VariableViewer variableViewer = new VariableViewer();

        final SplitPane root = new SplitPane(codeViewer, variableViewer);
        root.setOrientation(Orientation.HORIZONTAL);
        root.setDividerPositions(0);

        final AtomicBoolean refreshQueued = new AtomicBoolean(true);

        Runnable refresh = () -> {
            if (refreshQueued.get() && root.isVisible()) {
                codeViewer.refresh();
                variableViewer.refresh();
                refreshQueued.set(false);
            }
        };

        model.getCompilingProperty().addListener((evt, old, compiling) -> {
            if (compiling) {
                return;
            }
            refreshQueued.set(true);
            refresh.run();
        });

        root.visibleProperty().addListener((evt, old, v) -> refresh.run());
        return root;
    }

    /**
     * Create a new code editor
     * @return {@link CodeArea}
     * @apiNote The contents of this are directly bound to source code properties in {@link Model}
     * @see Model#getSourceCodeProperty()
     * @see Model#getSourceCode()
     */
    public Node createCodeEditor() {
        CodeArea area = new CodeArea(LocalStorage.getDefaultSource());
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        area.textProperty().addListener((evt, old, source) -> {
            if (!source.equals(model.getSourceCode())) {
                model.getSourceCodeProperty().set(source);
            }
        });

        area.setOnKeyReleased(event -> {
            if (Filter.isSpecialOperation(event)) {
                return;
            }

            KeyCode code = event.getCode();
            if (code == null) {
                return;
            }

            if (!KeyCode.SHIFT.equals(code) &&
                    !KeyCode.ALT.equals(code) &&
                    !KeyCode.CONTROL.equals(code) &&
                    !KeyCode.TAB.equals(code) &&
                    !KeyCode.CAPS.equals(code) &&
                    !KeyCode.BACK_SPACE.equals(code) &&
                    !code.isArrowKey()) {
                AutoComplete.fullfillPunctation(area);
            }
        });

        area.setOnKeyPressed(event -> {
            if (Filter.isSpecialOperation(event)) {
                return;
            }

            KeyCode code = event.getCode();
            if (KeyCode.ENTER.equals(code)) {
                AutoComplete.persistIndent(area);
            }
        });

        area.setOnKeyTyped(event -> {
            String character = event.getCharacter();
            if ("\t".equals(character)) {
                AutoComplete.formatIndent(area);
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
