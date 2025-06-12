package com.wyu4.snowberryjam.compiler.data.values.builtin;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceId;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Value Holder that requests a user input.
 */
public class InputHolder extends ValueHolder implements BuiltInHolder {
    private final AtomicInteger currentPointer = new AtomicInteger();
    private final AtomicReference<String> input = new AtomicReference<>();

    @Override
    public Object getValue() {
        if (!LocalStorage.isRunning()) {
            return "";
        }
        String lastInput = input.get();
        if (lastInput != null && LocalStorage.getPointer() == currentPointer.get()) {
            return lastInput;
        }

        final CompletableFuture<String> await = new CompletableFuture<>();
        LocalStorage.addInputSubscription(await::complete);
        while (LocalStorage.isRunning() && !Thread.currentThread().isInterrupted()) {
            try {
                String newInput = await.get(100, TimeUnit.MILLISECONDS);
                currentPointer.set(LocalStorage.getPointer());
                input.set(newInput);
                return newInput;
            } catch (TimeoutException | InterruptedException ignore) {} catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        await.cancel(false);
        return "";
    }

    /**
     * @return {@link SourceId#INPUT}
     */
    @Override
    public SourceId getId() {
        return SourceId.INPUT;
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
