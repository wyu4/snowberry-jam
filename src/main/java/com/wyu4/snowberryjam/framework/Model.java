package com.wyu4.snowberryjam.framework;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;

/**
 * The model of the MVCI framework.
 */
public class Model {
    private final ObjectProperty<File> sourceFileProperty = new SimpleObjectProperty<>();

    private final BooleanProperty saveDisabledProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty saveAsDisabledProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty runDisabledProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty compilingProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty runningProperty = new SimpleBooleanProperty(false);

    private final IntegerProperty logNumberProperty = new SimpleIntegerProperty(0);

    public ObjectProperty<File> getSourceFileProperty() {
        return sourceFileProperty;
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
        return compilingProperty;
    }

    public IntegerProperty getLogNumberProperty() {
        return logNumberProperty;
    }

    public File getSourceFile() {
        return getSourceFileProperty().get();
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

    public int getLogNumber() {
        return getLogNumberProperty().get();
    }
}
