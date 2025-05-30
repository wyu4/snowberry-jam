package com.wyu4.snowberryjam.Compiler;

import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.CoreElement;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.ExecutableTask;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.PrintTask;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class LocalStorage {
    private static Logger logger = null;

    private static final HashMap<String, Object> VARIABLES = new HashMap<>();
    private static final List<Thread> THREADS = new ArrayList<>();
    private static final BodyStack STACK = new BodyStack(SourceId.PROJECT);
    private static final AtomicReference<String> NAME = new AtomicReference<>("");

    public static Object getRaw(String name) throws NullPointerException {
        Object currentValue = VARIABLES.get(name);
        if (currentValue == null) {
            throw new NullPointerException("No variable with the name \"" + name + "\" was found.");
        }
        return currentValue;
    }

    public static void flush() {
        THREADS.forEach(Thread::interrupt);

        STACK.flush();
        VARIABLES.clear();
        THREADS.clear();
    }

    public static Thread createThread(Runnable run) {
        Thread thread = new Thread(run);
        THREADS.add(thread);
        return thread;
    }

    public static void setVariable(String name, Object newValue) throws NullPointerException {
        Object currentValue = getRaw(name);
        if (currentValue.getClass().equals(newValue.getClass())) {
            VARIABLES.put(name, newValue);
        }
    }

    protected static void runStack() {
        runStack(STACK);
    }

    protected static void setName(String name) {
        if (name == null) {
            name = "";
        }
        if (!name.equals(NAME.get())) {
            NAME.set(name);
            logger = null;
        }
    }

    private static void runStack(BodyStack stack) {
        stack.execute();
    }

    protected static void createVariable(String name, Object value) throws IllegalStateException {
        if (VARIABLES.containsKey(name)) {
            throw new IllegalStateException("Variable name \"" + name + "\" is already occupied.");
        }
        VARIABLES.put(name, value);
    }

    protected static void stackAdd(ExecutableTask element) {
        STACK.addElement(element);
    }

    private static Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(NAME.get());
        }
        return logger;
    }

    public static void print(Object message, Object... args) {
        getLogger().info(message.toString(), args);
    }
}
