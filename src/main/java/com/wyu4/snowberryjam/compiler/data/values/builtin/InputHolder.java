package com.wyu4.snowberryjam.compiler.data.values.builtin;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.Releasable;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Value Holder that requests a user input.
 */
public class InputHolder extends ValueHolder implements Releasable {
    private final AtomicReference<String> input = new AtomicReference<>();

    public InputHolder() {
        init();
    }

    @Override
    public Object getValue() {
        if (!LocalStorage.isRunning()) {
            return "";
        }

        final CompletableFuture<String> await = new CompletableFuture<>();
        LocalStorage.addInputSubscription(await::complete);
        while (LocalStorage.isRunning() && !Thread.currentThread().isInterrupted()) {
            try {
                return await.get(100, TimeUnit.MILLISECONDS);
            } catch (TimeoutException | InterruptedException ignore) {} catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        await.cancel(false);
        return "";
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
