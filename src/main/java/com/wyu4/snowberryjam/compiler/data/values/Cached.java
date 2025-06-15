package com.wyu4.snowberryjam.compiler.data.values;

import com.wyu4.snowberryjam.compiler.LocalStorage;

public interface Cached {
    default void init() {
        LocalStorage.addCachedValue(this);
    }
    void release();
}
