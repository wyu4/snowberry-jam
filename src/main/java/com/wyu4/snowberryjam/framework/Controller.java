package com.wyu4.snowberryjam.framework;

import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * The controller of the MVCI framework.
 */
public class Controller {
    private final Builder<Region> builder;

    /**
     * Create a new Controller
     */
    public Controller() {
        Model model = new Model();
        Interactor interactor = new Interactor(model);
        this.builder = new ViewBuilder(model);
    }

    /**
     * Get the primary view of the framework
     * @return {@link Region} containing the view built from the {@link ViewBuilder}
     */
    public Region getView() {
        return builder.build();
    }
}
