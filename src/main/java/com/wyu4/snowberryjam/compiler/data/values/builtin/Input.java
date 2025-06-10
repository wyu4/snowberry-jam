package com.wyu4.snowberryjam.compiler.data.values.builtin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.Releasable;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

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
        if (input.get() == null) {
            final CompletableFuture<String> await = new CompletableFuture<>();
            LocalStorage.addInputSubscription(await::complete);
            input.set(await.join());
        }
        return input.get();
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
