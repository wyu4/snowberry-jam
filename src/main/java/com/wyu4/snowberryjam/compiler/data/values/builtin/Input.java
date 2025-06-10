package com.wyu4.snowberryjam.compiler.data.values.builtin;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.Releasable;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Value Holder that requests a user input.
 */
public class Input extends ValueHolder implements Releasable {
    private final AtomicReference<String> input = new AtomicReference<>();

    public Input() {
        init();
    }

    @Override
    public Object getValue() {
        final CompletableFuture<String> await = new CompletableFuture<>();
        LocalStorage.addInputSubscription(await::complete);
        return await.join();
    }

    public void release() {
        input.set(null);
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
        return "an input from the user";
    }
}
