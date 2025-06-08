package com.wyu4.snowberryjam.codeutils;

import org.fxmisc.richtext.CodeArea;

import javafx.application.Platform;
import javafx.scene.input.KeyEvent;

public abstract class AutoComplete {
    public static void persistIndent(CodeArea area) {
        int caretPos = area.getCaretPosition();
        int currentParagraph = area.getCurrentParagraph();

        if (currentParagraph > 0) {
            String prevLine = area.getParagraph(currentParagraph - 1).getText();

            int i = 0;
            while (i < prevLine.length() && (prevLine.charAt(i) == ' ' || prevLine.charAt(i) == '\t')) {
                i++;
            };
            insertBefore(area, prevLine.substring(0, i), caretPos);
        }
    }

    public static void formatIndent(CodeArea area) {
        replaceBefore(area, "    ", area.getCaretPosition());
    }

    public static void fullfillPunctation(CodeArea area) {
        int cursorPosition = area.getCaretPosition();
        if (cursorPosition > 0) {
            char code = area.getText(cursorPosition - 1, cursorPosition).charAt(0);
            switch (code) {
                case '[' -> insertAfter(area, ']', cursorPosition);
                case '{' -> insertAfter(area, '}', cursorPosition);
                case '"' -> insertAfter(area, '"', cursorPosition);
                case '\'' -> insertAfter(area, '\'', cursorPosition);
            }
        }
    }

    public static void replaceBefore(CodeArea area, Object content, int cursorPos) {
        replace(area, content, cursorPos-1, cursorPos);
    }

    public static void replace(CodeArea area, Object content, int start, int end) {
        Platform.runLater(() -> {
            area.replaceText(start, end, String.valueOf(content));
        });
    }

    public static void insertBefore(CodeArea area, Object content, int position) {
        Platform.runLater(() -> {
            area.insertText(position, String.valueOf(content));
        });
    }

    public static void insertAfter(CodeArea area, Object content, int position) {
        Platform.runLater(() -> {
            area.insertText(position, String.valueOf(content));
            area.moveTo(position);
        });
    }
}
