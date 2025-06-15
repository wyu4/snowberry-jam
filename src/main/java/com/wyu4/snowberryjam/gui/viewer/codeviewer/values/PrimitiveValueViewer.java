package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;

import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class PrimitiveValueViewer extends ValueViewer {
    public PrimitiveValueViewer(ValueHolder value) {
        super(value);
        setBackground(new Background(new BackgroundFill(ColorDictionary.getColorFromId(value.getId()), SQUARE_RADII, Insets.EMPTY)));
        applyBorderWithRadii(SQUARE_RADII);
        getChildren().add(new Label(value.toString()));
    }
}
