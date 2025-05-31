package com.wyu4.snowberryjam.Compiler.Helpers;

public abstract class EnumHelper {
    public static SourceId stringToId(String str) {
        for (SourceId id : SourceId.values()) {
            if (id.toString().equals(str)) {
                return id;
            }
        }
        return null;
    }

    public static SourceKey stringToKey(String str) {
        for (SourceKey id : SourceKey.values()) {
            if (id.toString().equals(str)) {
                return id;
            }
        }
        return null;
    }
}
