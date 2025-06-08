package com.wyu4.snowberryjam.framework;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;

import java.io.File;

/**
 * The controller of the MVCI framework.
 */
public class Controller {
    private final Builder<Region> builder;

    /**
     * Create a new Controller
     * @param stage The primary {@link Stage} of the window
     */
    public Controller(Stage stage) {
        this(stage, null);
    }

    /**
     * Create a new Controller
     * @param file Source file to open
     * @param stage The primary {@link Stage} of the window
     */
    public Controller(Stage stage, File file) {
        Model model = new Model();
        Interactor interactor = new Interactor(model, stage);
        this.builder = new ViewBuilder(model, interactor);
        if (file == null) {
            model.getSourceCodeProperty().set(LocalStorage.getDefaultSource());
        } else {
            model.getSourceFileProperty().set(file);
        }
    }

    /**
     * Get the primary view of the framework
     * @return {@link Region} containing the view built from the {@link ViewBuilder}
     */
    public Region getView() {
        return builder.build();
    }
}
