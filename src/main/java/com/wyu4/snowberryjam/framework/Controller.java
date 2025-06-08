package com.wyu4.snowberryjam.framework;

import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.wyu4.snowberryjam.compiler.LocalStorage;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;

import java.io.File;

/**
 * The controller of the MVCI framework.
 */
public class Controller {
    private final Builder<Region> builder;
    private final Model model;
    private final Interactor interactor;

    /**
     * Create a new Controller
     * @param stage The primary {@link Stage} of the window
     */
    public Controller(Stage stage) {
        this.model = new Model();
        this.interactor = new Interactor(model, stage);
        this.builder = new ViewBuilder(model, interactor);
    }

    public void updateFile(File file) {
        if (file == null) {
            model.getSourceCodeProperty().set(LocalStorage.getDefaultSource());
        } else {
            interactor.createSetFileTask(file).run();
        }
    }

    public File getSourceFile() {
        return model.getSourceFile();
    }

    public ObjectProperty<File> getSourceFileProperty() {
        return model.getSourceFileProperty();
    }

    /**
     * Get the primary view of the framework
     * @return {@link Region} containing the view built from the {@link ViewBuilder}
     */
    public Region getView() {
        return builder.build();
    }
}
