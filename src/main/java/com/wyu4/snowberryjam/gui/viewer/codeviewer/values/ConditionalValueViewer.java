package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;


import com.wyu4.snowberryjam.compiler.data.values.conditional.ConditionalHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class ConditionalValueViewer extends ValueViewer {
    public ConditionalValueViewer(ConditionalHolder value) {
        super(value);
        SourceId id = value.getId();

        setBackground(new Background(new BackgroundFill(ColorDictionary.getColorFromId(id), CIRCULAR_RADII, Insets.EMPTY)));
        applyBorderWithRadii(CIRCULAR_RADII);
        setPadding(new Insets(5, 20, 5, 20));

        if (id.equals(SourceId.NOT)) {
            getChildren().addAll(new Label(id.getBeautified()), ValueViewer.buildValueViewer(value.getA()));
        }
        getChildren().addAll(ValueViewer.buildValueViewer(value.getA()), new Label(id.getBeautified()), ValueViewer.buildValueViewer(value.getB()));
    }
}
