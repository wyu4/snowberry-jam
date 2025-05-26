package com.wyu4.snowberryjam.Framework;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class Controller {
    private final Builder<Region> builder;

    public Controller() {
        Model model = new Model();
        Interactor interactor = new Interactor(model);
        this.builder = new ViewBuilder(model);
    }

    public Region getView() {
        return builder.build();
    }
}
