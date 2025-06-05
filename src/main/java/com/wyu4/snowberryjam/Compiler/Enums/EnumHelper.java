package com.wyu4.snowberryjam.Compiler.Enums;

import com.wyu4.snowberryjam.Compiler.Compiler;

/**
 * Helper methods related to value Enums
 * @see SourceId
 * @see SourceKey
 */
public abstract class EnumHelper {

    /**
     * Convert a string to a {@link SourceId}
     * @param str String
     * @return Corresponding {@link SourceId}. May be {@code null} if the string doesn't match any enums.
     */
    public static SourceId stringToId(String str) {
        for (SourceId id : SourceId.values()) {
            if (id.toString().equals(str)) {
                return id;
            }
        }
        Compiler.warn("ID \"{}\" is not registered.", str);
        return null;
    }

    /**
     * Convert a string to a {@link SourceKey}
     * @param str String
     * @return Corresponding {@link SourceKey}. May be {@code null} if the string doesn't match any enums.
     */
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
