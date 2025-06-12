package com.wyu4.snowberryjam.compiler.data.values.builtin;

import com.wyu4.snowberryjam.ResourceUtils;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

/**
 * Value Holder that provides time resources.
 */
public class PublicFolderHolder extends ValueHolder {
    /**
     * @return The full path to the public folder
     * @see ResourceUtils#PUBLIC
     */
    @Override
    public Object getValue() {
        return ResourceUtils.PUBLIC;
    }

    /**
     * @return {@link SourceId#PUBLIC_FOLDER}
     */
    @Override
    public SourceId getId() {
        return SourceId.PUBLIC_FOLDER;
    }

    /**
     * @return Always returns {@link String}
     */
    @Override
    public Class<?> getType() {
        return String.class;
    }

    @Override
    public String toString() {
        return "the full path to the public folder";
    }
}
