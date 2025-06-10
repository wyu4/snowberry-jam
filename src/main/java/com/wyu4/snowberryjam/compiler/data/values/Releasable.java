package com.wyu4.snowberryjam.compiler.data.values;

import com.wyu4.snowberryjam.compiler.LocalStorage;

public interface Releasable {
    default void init() {
        LocalStorage.addReleasable(this);
    }

    void release();
}