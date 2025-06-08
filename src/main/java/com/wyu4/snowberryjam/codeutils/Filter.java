package com.wyu4.snowberryjam.codeutils;

import javafx.scene.input.KeyEvent;

public abstract class Filter {
    public static boolean isSpecialOperation(KeyEvent event) {
        return event.isControlDown() || event.isAltDown() || event.isMetaDown();
    }
}
