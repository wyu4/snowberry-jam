package com.wyu4.snowberryjam.framework.viewer;

import com.wyu4.snowberryjam.compiler.enums.SourceId;

import javafx.scene.paint.Color;

public abstract class ColorDictionary {
    private static final Color BUILT_IN = Color.rgb(254, 220, 189);
    private static final Color MATH = Color.rgb(171, 224, 253);
    private static final Color IF_STATEMENT = Color.rgb(247, 253, 171);
    private static final Color MACRO = Color.rgb(171, 174, 253);
    private static final Color LOOP = Color.rgb(208, 199, 255);
    private static final Color DELAY = Color.rgb(123, 191, 255);
    private static final Color STORAGE = Color.rgb(212, 255, 222);
    private static final Color CONDITIONAL = Color.rgb(103, 255, 134);
    private static final Color THREADING = Color.rgb(223, 223, 223);
    private static final Color CONVERSION = Color.rgb(255, 116, 255);
    private static final Color IO = Color.rgb(246, 255, 116);
    private static final Color ITERATION = Color.rgb(116, 255, 211);
    private static final Color BLAND = Color.rgb(0, 0, 0, 0);

    public static Color getColorFromId(SourceId id) {
        return switch(id) {
            case AND -> CONDITIONAL;
            case ARRAY_OF -> CONVERSION;
            case DECREASE_MACRO -> MACRO;
            case DIVIDE -> MATH;
            case ELEMENT_AT_INDEX -> ITERATION;
            case EQUALS -> CONDITIONAL;
            case ERROR -> Color.rgb(255, 108, 108);
            case GREATER_OR_EQUAL_TO -> CONDITIONAL;
            case GREATER_THAN -> CONDITIONAL;
            case IF -> IF_STATEMENT;
            case IF_ELSE -> IF_STATEMENT;
            case INCREASE_MACRO -> MACRO;
            case INPUT -> BUILT_IN;
            case INPUT_MACRO -> MACRO;
            case LESS_OR_EQUAL_TO -> CONDITIONAL;
            case LESS_THAN -> CONDITIONAL;
            case MINUS -> MATH;
            case MODULUS -> MATH;
            case MULTIPLY -> MATH;
            case NOT -> CONDITIONAL;
            case NOT_EQUALS -> CONDITIONAL;
            case ON_RUN -> BLAND;
            case OR -> CONDITIONAL;
            case PARSE_NUMBER -> CONVERSION;
            case PLUS -> MATH;
            case PRINT -> Color.rgb(255, 251, 221);
            case PROJECT -> BLAND;
            case PUBLIC_FOLDER -> BUILT_IN;
            case RANDOM -> MATH;
            case READ_FILE -> IO;
            case REPEAT -> LOOP;
            case ROUND -> MATH;
            case SAME_TYPE -> CONDITIONAL;
            case SET -> STORAGE;
            case SIZE_OF -> CONVERSION;
            case SPLIT -> CONVERSION;
            case THREAD -> THREADING;
            case TIME -> MATH;
            case VARIABLE -> STORAGE;
            case WAIT -> DELAY;
            case WAIT_UNTIL -> DELAY;
            case WARN -> Color.rgb(255, 204, 52);
            case WHILE -> LOOP;
            case WITH_UPDATED_ELEMENT -> ITERATION;
        };
    }
}
