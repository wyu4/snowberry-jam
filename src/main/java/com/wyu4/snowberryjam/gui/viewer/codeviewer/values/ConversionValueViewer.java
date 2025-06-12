package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;

import com.wyu4.snowberryjam.compiler.data.values.conversion.ConversionHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public class ConversionValueViewer extends StackPane {
    public ConversionValueViewer(ConversionHolder value) {
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxWidth(Region.USE_PREF_SIZE);
        setMinHeight(Region.USE_PREF_SIZE);
        setMaxHeight(Region.USE_PREF_SIZE);

        ValueViewer content = new ValueViewer(value);

        SourceId id = value.getId();

        content.setPadding(new Insets(5, 20, 5, 20));

        Polygon chamferClip = new Polygon();
        chamferClip.setManaged(false);
        chamferClip.setFill(ColorDictionary.getColorFromId(id));
        chamferClip.setStroke(ValueViewer.BORDER_COLOR);
        chamferClip.setStrokeWidth(1);
        

        final double inset = 15;
        content.layoutBoundsProperty().addListener((evt, old, bounds) -> {
            double width = content.getWidth();
            double height = content.getHeight();

            chamferClip.getPoints().setAll(
                0D, inset,
                inset, 0D,
                width - inset, 0D,
                width, inset,
                width, height - inset,
                width - inset, height,
                inset, height,
                0D, height - inset
            );
        });

        getChildren().addAll(chamferClip, content);

        if (id.equals(SourceId.PARSE_NUMBER)) {
            content.getChildren().addAll(ValueViewer.buildValueViewer(value.getA()), new Label(id.getBeautified()));
            return;
        }
        if (id.equals(SourceId.SPLIT)) {
            content.getChildren().addAll(ValueViewer.buildValueViewer(value.getA()), new Label(id.getBeautified()), ValueViewer.buildValueViewer(value.getB()));
            return;
        }
        content.getChildren().addAll(new Label(id.getBeautified()), ValueViewer.buildValueViewer(value.getA()));
    }
}
