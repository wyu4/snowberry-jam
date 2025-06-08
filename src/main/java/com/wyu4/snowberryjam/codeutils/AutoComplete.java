package com.wyu4.snowberryjam.codeutils;

import org.fxmisc.richtext.CodeArea;

import javafx.application.Platform;
import javafx.scene.input.KeyEvent;

public abstract class AutoComplete {
    public static void autoIndent(CodeArea area) {
        int caretPos = area.getCaretPosition();
        int currentParagraph = area.getCurrentParagraph();

        if (currentParagraph > 0) {
            String prevLine = area.getParagraph(currentParagraph - 1).getText();

            int i = 0;
            while (i < prevLine.length() && (prevLine.charAt(i) == ' ' || prevLine.charAt(i) == '\t')) {
                i++;
            };
            insertContentBefore(area, prevLine.substring(0, i), caretPos);
        }
    }

    public static void formatIndent(CodeArea area, KeyEvent event) {
        event.consume();
        area.insertText(area.getCaretPosition(), "    "); // Insert 4 spaces
    }

    public static void fullfillPunctation(CodeArea area) {
        int cursorPosition = area.getCaretPosition();
        if (cursorPosition > 0) {
            char code = area.getText(cursorPosition - 1, cursorPosition).charAt(0);
            switch (code) {
                case '[' -> insertContentAfter(area, ']', cursorPosition);
                case '{' -> insertContentAfter(area, '}', cursorPosition);
                case '"' -> insertContentAfter(area, '"', cursorPosition);
                case '\'' -> insertContentAfter(area, '\'', cursorPosition);
            }
        }
    }

    public static void insertContentBefore(CodeArea area, Object content, int position) {
        Platform.runLater(() -> {
            area.insertText(position, String.valueOf(content));
        });
    }

    public static void insertContentAfter(CodeArea area, Object content, int position) {
        Platform.runLater(() -> {
            area.insertText(position, String.valueOf(content));
            area.moveTo(position);
        });
    }
}
