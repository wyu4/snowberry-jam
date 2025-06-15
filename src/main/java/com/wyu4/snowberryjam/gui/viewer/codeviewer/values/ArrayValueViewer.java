package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;

import com.wyu4.snowberryjam.compiler.data.values.iteration.ArrayHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class ArrayValueViewer extends ValueViewer {
    public ArrayValueViewer(ArrayHolder value) {
        super(value);
        SourceId id = value.getId();

        setBackground(new Background(new BackgroundFill(ColorDictionary.getColorFromId(id), CIRCULAR_RADII, Insets.EMPTY)));
        applyBorderWithRadii(CIRCULAR_RADII);
        setPadding(new Insets(5, 20, 5, 20));

        getChildren().addAll(
                new Label("index"),
                ValueViewer.buildValueViewer(value.getI()),
                new Label("of"),
                ValueViewer.buildValueViewer(value.getA())
            );

        if (id.equals(SourceId.WITH_UPDATED_ELEMENT)) {
            getChildren().addAll(
                new Label("swapped with"),
                ValueViewer.buildValueViewer(value.getB())
            );
        }
    }
}
