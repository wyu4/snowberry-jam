package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;

import java.util.Arrays;

import com.wyu4.snowberryjam.compiler.data.values.math.ArithmeticHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.gui.viewer.codeviewer.ColorDictionary;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

public class ArithmeticValueViewer extends StackPane {
    private static final SourceId[] MATH_FUNCTIONS = {
        SourceId.ROUND,
        SourceId.ARCCOSINE,
        SourceId.ARCSINE,
        SourceId.ARCTANGENT,
        SourceId.SINE,
        SourceId.COSINE,
        SourceId.TANGENT
    };

    public ArithmeticValueViewer(ArithmeticHolder value) {
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
            double halfHeight = height / 2;

            chamferClip.getPoints().setAll(
                    0D, halfHeight,
                    inset, 0D,
                    width - inset, 0D,
                    width, height / 2,
                    width - inset, height,
                    inset, height
            );
        });

        getChildren().addAll(chamferClip, content);

        if (Arrays.stream(MATH_FUNCTIONS).anyMatch(fid -> fid.equals(id))) {
            content.getChildren().addAll(new Label(id.getBeautified()), ValueViewer.buildValueViewer(value.getA()));
            return;
        }
        if (id.equals(SourceId.ROOT)) {
            final VBox baseNode = new VBox();
            baseNode.setPadding(new Insets(0, 0, 15, 0));
            baseNode.getChildren().add(ValueViewer.buildValueViewer(value.getB()));
            content.getChildren().addAll(
                baseNode,
                new Label(id.getBeautified()),
                ValueViewer.buildValueViewer(value.getA()));
            return;
        }
        content.getChildren().addAll(
                ValueViewer.buildValueViewer(value.getA()),
                new Label(id.getBeautified()),
                ValueViewer.buildValueViewer(value.getB()));
    }
}
