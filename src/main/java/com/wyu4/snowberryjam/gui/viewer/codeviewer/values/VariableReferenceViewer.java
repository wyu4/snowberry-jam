package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;


import com.wyu4.snowberryjam.compiler.data.values.VariableReference;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class VariableReferenceViewer extends ValueViewer {
    public VariableReferenceViewer(VariableReference value) {
        super(value);
        SourceId id = value.getId();

        setBackground(new Background(new BackgroundFill(ColorDictionary.getColorFromId(id), CIRCULAR_RADII, Insets.EMPTY)));
        applyBorderWithRadii(CIRCULAR_RADII);
        setPadding(new Insets(5, 20, 5, 20));

        getChildren().addAll(new Label("variable"), ValueViewer.buildValueViewer(value.getName()));
    }
}
