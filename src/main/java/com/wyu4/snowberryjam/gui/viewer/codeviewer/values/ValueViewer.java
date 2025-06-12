package com.wyu4.snowberryjam.gui.viewer.codeviewer.values;

import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.VariableReference;
import com.wyu4.snowberryjam.compiler.data.values.builtin.BuiltInHolder;
import com.wyu4.snowberryjam.compiler.data.values.conditional.ConditionalHolder;
import com.wyu4.snowberryjam.compiler.data.values.iteration.ArrayHolder;
import com.wyu4.snowberryjam.compiler.data.values.math.ArithmeticHolder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class ValueViewer extends HBox {
    public static final Color BORDER_COLOR = Color.rgb(0, 0, 0, 0.5);
    public static final CornerRadii SQUARE_RADII = new CornerRadii(3);
    public static final CornerRadii CIRCULAR_RADII = new CornerRadii(999);

    public static Node buildValueViewer(ValueHolder holder) {
        if (holder instanceof VariableReference parsed) {
            return new VariableReferenceViewer(parsed);
        }
        if (holder instanceof ArrayHolder parsed) {
            return new ArrayValueViewer(parsed);
        }
        if (holder instanceof ArithmeticHolder parsed) {
            return new ArithmeticValueViewer(parsed);
        }
        if (holder instanceof ConditionalHolder parsed) {
            return new ConditionalValueViewer(parsed);
        }
        if (holder instanceof BuiltInHolder) {
            return new BuiltInViewer(holder);
        }
        return new PrimitiveValueViewer(holder);
    }

    private final ValueHolder value;

    public ValueViewer(ValueHolder value) {
        super(5);
        this.value = value;
        getStyleClass().add("value-viewer");
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(5, 5, 5, 5));
        setMinWidth(Region.USE_PREF_SIZE);
        setMaxWidth(Region.USE_PREF_SIZE);
        setMinHeight(Region.USE_PREF_SIZE);
        setMaxHeight(Region.USE_PREF_SIZE);
    }

    public ValueHolder getValue() {
        return value;
    }

    protected void applyBorderWithRadii(CornerRadii radii) {
        setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, radii, BorderWidths.DEFAULT)));
    }
}
