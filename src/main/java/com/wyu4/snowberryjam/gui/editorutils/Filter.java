package com.wyu4.snowberryjam.gui.editorutils;

import javafx.scene.input.KeyEvent;

public abstract class Filter {
    public static boolean isSpecialOperation(KeyEvent event) {
        return event.isControlDown() || event.isAltDown() || event.isMetaDown();
    }

    public static boolean isWrappablePunctuation(KeyEvent event) {
        String code = event.getCharacter();
        return "{".equals(code) || "[".equals(code) || "'".equals(code) || "\"".equals(code);
    }
}
