package com.wyu4.snowberryjam.Compiler;

import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.ExecutableTask;
import com.wyu4.snowberryjam.Compiler.Enums.SourceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class stores and handles anything related to compiling and running a source file.
 */
public abstract class LocalStorage {
    private static Logger logger = null;

    /** Stores variables */
    private static final HashMap<String, Object> VARIABLES = new HashMap<>();
    /** Stores threads */
    private static final List<Thread> THREADS = new ArrayList<>();
    /** Stores tasks starting from index 0 */
    private static final BodyStack STACK = new BodyStack(SourceId.PROJECT);
    /** Atomically stores the project name */
    private static final AtomicReference<String> NAME = new AtomicReference<>("");

    /**
     * Get the raw value of a stored variable
     * @param name The name of a variable
     * @return The value of the variable as an {@link Object}
     * @throws NullPointerException If the variable was not created
     * @see #createVariable(String, Object)
     */
    public static Object getRaw(String name) throws NullPointerException {
        Object currentValue = VARIABLES.get(name);
        if (currentValue == null) {
            throw new NullPointerException("No variable with the name \"" + name + "\" was found.");
        }
        return currentValue;
    }

    /**
     * Flush all the contents of the {@link LocalStorage}. This includes {@link LocalStorage#STACK}, {@link #VARIABLES}, and {@link #THREADS} (after interrupting them).
     */
    public static void flush() {
        THREADS.forEach(Thread::interrupt);

        STACK.flush();
        VARIABLES.clear();
        THREADS.clear();
    }

    /**
     * Create a thread and store it in {@link #THREADS}
     * @param run The code to run in the thread
     * @return A {@link Thread} object initialized with {@code run}
     */
    public static Thread createThread(Runnable run) {
        Thread thread = new Thread(run);
        THREADS.add(thread);
        return thread;
    }

    /**
     * Override the contents of a variable in {@link #VARIABLES}
     * @param name The name of the (already created) variable
     * @param newValue The new value
     * @throws NullPointerException If the variable was not created
     * @see #createVariable(String, Object)
     */
    public static void setVariable(String name, Object newValue) throws NullPointerException {
        Object currentValue = getRaw(name);
        if (currentValue.getClass().equals(newValue.getClass())) {
            VARIABLES.put(name, newValue);
        }
    }

    /**
     * Run {@link #STACK}
     * @see BodyStack#execute()
     */
    protected static void runStack() {
        STACK.execute();
    }

    /**
     * Set the project name
     * @param name Project name
     */
    protected static void setName(String name) {
        if (name == null) {
            name = "";
        }
        if (!name.equals(NAME.get())) {
            NAME.set(name);
            logger = null;
        }
    }

    /**
     * Create a variable and store it in {@link #VARIABLES}
     * @param name The name of the variable
     * @param value The value of the variable
     * @throws IllegalStateException A variable with the same name already exists
     */
    protected static void createVariable(String name, Object value) throws IllegalStateException {
        if (VARIABLES.containsKey(name)) {
            throw new IllegalStateException("Variable name \"" + name + "\" is already occupied.");
        }
        VARIABLES.put(name, value);
    }

    /**
     * Add a task to {@link #STACK}
     * @param element A task
     * @see ExecutableTask
     */
    protected static void stackAdd(ExecutableTask element) {
        STACK.addTask(element);
    }

    /**
     * Get the logger with the current project name. Creates a new logger with every name change.
     * @return The current logger
     * @see #NAME
     * @see #logger
     * @see Logger
     */
    private static Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(NAME.get());
        }
        return logger;
    }

    /**
     * Send a print statement to the runtime console.
     * @param message Message to send (arguments can be inserted by adding "{}" in the message)
     * @param args Arguments to append in the message
     * @see #getLogger()
     */
    public static void print(Object message, Object... args) {
        getLogger().info(message.toString(), args);
    }

    /**
     * Send a print statement to the runtime console.
     * @param message Message to send (arguments can be inserted by adding "{}" in the message)
     * @param args Arguments to append in the message
     * @see #getLogger()
     */
    public static void warn(Object message, Object... args) {
        getLogger().warn(message.toString(), args);
    }
}
