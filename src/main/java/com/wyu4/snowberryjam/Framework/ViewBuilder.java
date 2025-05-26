package com.wyu4.snowberryjam.Framework;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBuilder implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(ViewBuilder.class);
    private final Model model;

    public ViewBuilder(Model model) {
        this.model = model;
    }

    @Override
    public Region build() {
        return new StackPane();
    }


}
