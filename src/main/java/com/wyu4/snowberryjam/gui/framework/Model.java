package com.wyu4.snowberryjam.gui.framework;

import javafx.beans.property.*;

import java.io.File;

/**
 * The model of the MVCI framework.
 */
public class Model {
    public enum Page {
        EDITOR, VIEWER
    }

    private final ObjectProperty<File> sourceFileProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Page> pageProperty = new SimpleObjectProperty<>(Page.EDITOR);

    private final BooleanProperty saveDisabledProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty saveAsDisabledProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty runDisabledProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty compilingProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty runningProperty = new SimpleBooleanProperty(false);

    private final StringProperty sourceCodeProperty = new SimpleStringProperty("");
    private final StringProperty builtSourceCodeProperty = new SimpleStringProperty("");

    public Model() {
        runDisabledProperty.bind(compilingProperty);
    }

    public ObjectProperty<File> getSourceFileProperty() {
        return sourceFileProperty;
    }

    public ObjectProperty<Page> getPageProperty() {
        return pageProperty;
    }

    public BooleanProperty getSaveDisabledProperty() {
        return saveDisabledProperty;
    }

    public BooleanProperty getSaveAsDisabledProperty() {
        return saveAsDisabledProperty;
    }

    public BooleanProperty getRunDisabledProperty() {
        return runDisabledProperty;
    }

    public BooleanProperty getCompilingProperty() {
        return compilingProperty;
    }

    public BooleanProperty getRunningProperty() {
        return runningProperty;
    }

    public StringProperty getSourceCodeProperty() {
        return sourceCodeProperty;
    }

    public StringProperty getBuiltSourceCodeProperty() {
        return builtSourceCodeProperty;
    }

    public File getSourceFile() {
        return getSourceFileProperty().get();
    }

    public Page getPage() {
        return getPageProperty().get();
    }

    public boolean getSaveDisabled() {
        return getSaveDisabledProperty().get();
    }

    public boolean getSaveAsDisabled() {
        return getSaveAsDisabledProperty().get();
    }

    public boolean getRunDisabled() {
        return getRunDisabledProperty().get();
    }

    public boolean getCompiling() {
        return getCompilingProperty().get();
    }

    public boolean getRunning() {
        return getRunningProperty().get();
    }

    public String getSourceCode() {
        return getSourceCodeProperty().get();
    }

    public String getBuiltSourceCode() {
        return getBuiltSourceCodeProperty().get();
    }
}
