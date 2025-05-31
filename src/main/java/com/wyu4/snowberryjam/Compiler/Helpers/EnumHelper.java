package com.wyu4.snowberryjam.Compiler.Helpers;

import com.wyu4.snowberryjam.Compiler.Compiler;

public abstract class EnumHelper {
    public static SourceId stringToId(String str) {
        for (SourceId id : SourceId.values()) {
            if (id.toString().equals(str)) {
                return id;
            }
        }
        Compiler.warn("ID \"{}\" is not registered.", str);
        return null;
    }

    public static SourceKey stringToKey(String str) {
        for (SourceKey id : SourceKey.values()) {
            if (id.toString().equals(str)) {
                return id;
            }
        }
        Compiler.warn("Key \"{}\" is not registered.", str);
        return null;
    }
}
