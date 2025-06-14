package com.wyu4.snowberryjam.compiler;

import com.wyu4.snowberryjam.ResourceUtils;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.ExecutableTask;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This class stores and handles anything related to compiling and running a
 * source file.
 */
public abstract class LocalStorage {
    private static AtomicReference<Logger> logger = new AtomicReference<>();
    private static final Logger inputLogger = LoggerFactory.getLogger("Input");

    /** Stores variables */
    private static final HashMap<String, Object> VARIABLES = new HashMap<>();

    /** A new copy of the variables. This is used during the runtime. */
    private static final HashMap<String, Object> VARIABLES_COPY = new HashMap<>();

    /** Stores threads */
    private static final List<Thread> THREADS = new ArrayList<>();
    /** Stores tasks starting from index 0 */
    private static final BodyStack STACK = new BodyStack(SourceId.PROJECT);
    /** Atomically stores the project name */
    private static final AtomicReference<String> NAME = new AtomicReference<>("");

    /** The description of the project */
    private static final AtomicReference<String> DESCRIPTION = new AtomicReference<>("");

    /** The print listeners */
    private static final List<BiConsumer<String, String>> PRINT_LISTENERS = new ArrayList<>();

    /** The warn listeners */
    private static final List<BiConsumer<String, String>> WARN_LISTENERS = new ArrayList<>();

    /** The error listeners */
    private static final List<BiConsumer<String, String>> ERROR_LISTENERS = new ArrayList<>();

    /** The input listeners/subscribers */
    private static final List<Consumer<String>> INPUT_LISTENERS = new ArrayList<>();

    /** THe variable listeners */
    private static final HashMap<String, Consumer<Object>> VARIABLE_LISTENERS = new HashMap<>();

    /** The runtime pointer index */
    private static final AtomicInteger pointer = new AtomicInteger(0);

    /** The running property */
    private static final AtomicBoolean running = new AtomicBoolean(false);

    /** If the runtime was manually stopped prematurely by the user */
    private static final AtomicBoolean manualStop = new AtomicBoolean(false);

    /**
     * Get a default project template.
     * @return {@link String} containing a valid default empty project.
     */
    public static String getDefaultSource() {
        return ResourceUtils.readFile(ResourceUtils.ResourceFile.DEFAULT_SOURCE);
    }

    /**
     * Get the raw value of a stored variable
     * 
     * @param name The name of a variable
     * @return The value of the variable as an {@link Object}
     * @throws NullPointerException If the variable was not created
     * @see #createVariable(String, Object)
     */
    public static Object getRaw(String name) throws NullPointerException {
        Object currentValue = VARIABLES_COPY.get(name);
        if (currentValue == null) {
            throw new NullPointerException("No variable with the name \"" + name + "\" was found.");
        }
        return currentValue;
    }

    /**
     * Get all of variable names.
     * @return {@link Set} with created variable names
     */
    public static Set<String> getVariableNames() {
        return VARIABLES.keySet();
    }

    /**
     * Recieve a deep copy of the tasks inside a stack
     * @return An array of the tasks in a stack
     * @see BodyStack#copyStack()
     */
    public static ExecutableTask[] copyStack() {
        return STACK.copyStack();
    }

    /**
     * Flush all the contents of the {@link LocalStorage}. This includes
     * {@link LocalStorage#STACK}, {@link #VARIABLES}, and {@link #THREADS} (after
     * interrupting them).
     */
    public static void flush() {
        resetPointer();

        THREADS.forEach(Thread::interrupt);

        STACK.flush();
        VARIABLES.clear();
        VARIABLES_COPY.clear();
        THREADS.clear();

        INPUT_LISTENERS.forEach(consumer -> consumer.accept(null));
        INPUT_LISTENERS.clear();

        setName(null);
        setDescription(null);
    }

    /**
     * Create a thread and store it in {@link #THREADS}
     * 
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
     * 
     * @param name     The name of the (already created) variable
     * @param newValue The new value
     * @throws NullPointerException If the variable was not created
     * @see #createVariable(String, Object)
     */
    public static void setVariable(String name, Object newValue) throws NullPointerException {
        if (newValue instanceof Number parsed) {
            newValue = parsed.doubleValue();
        }

        if (!VARIABLES_COPY.containsKey(name)) {
            throw new NullPointerException("Variable \"%s\" was never created.".formatted(name));
        }
        VARIABLES_COPY.put(name, newValue);

        Consumer<Object> listener = VARIABLE_LISTENERS.get(name);
        if (listener != null) {
            listener.accept(newValue);
        }
    }

    /**
     * Run {@link #STACK}
     * 
     * @see BodyStack#execute()
     */
    public static void runStack() {
        if (running.get()) {
            return;
        }
        running.set(true);
        manualStop.set(false);
        resetPointer();
        try {
            VARIABLES_COPY.putAll(VARIABLES);
            VARIABLES_COPY.forEach((name, value) -> {
                Consumer<Object> consumer = VARIABLE_LISTENERS.get(name);
                if (consumer != null) {
                    consumer.accept(value);
                }
            });
            print("-----------------------------");

            STACK.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            running.set(false);
        }

        if (manualStop.get()) {
            warn("Manually Stopped.");
        }
    }

    /**
     * Stop the current run
     */
    public static void stopRun() {
        manualStop.set(true);
        running.set(false);
        THREADS.forEach(Thread::interrupt);
    }

    /**
     * Check if the stack is running
     * @return {@code true} if the LocalStorage is running the stack, otherwise {@code false}.
     */
    public static boolean isRunning() {
        return running.get();
    }

    /**
     * Set the project name
     * 
     * @param name Project name
     */
    protected static void setName(String name) {
        if (name == null) {
            name = "";
        }
        if (!name.equals(NAME.get())) {
            NAME.set(name);
            logger.set(null);
        }
    }

    /**
     * Set the description of the project.
     * @param description Description of the project
     */
    protected static void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        DESCRIPTION.set(description);
    }

    /**
     * Create a variable and store it in {@link #VARIABLES}
     * 
     * @param name  The name of the variable
     * @param value The value of the variable
     * @throws IllegalStateException A variable with the same name already exists
     */
    protected static void createVariable(String name, Object value) throws IllegalStateException {
        if (VARIABLES.containsKey(name)) {
            throw new IllegalStateException("Variable name \"" + name + "\" is already occupied.");
        }
        VARIABLES.put(name, value);
        VARIABLES_COPY.put(name, value);
    }

    /**
     * Add a task to {@link #STACK}
     * 
     * @param element A task
     * @see ExecutableTask
     */
    protected static void stackAdd(ExecutableTask element) {
        STACK.addTask(element);
    }

    public static void increasePointer() {
        pointer.set(pointer.get() + 1);
    }

    /**
     * Get the pointer index.
     * @return The current pointer index.
     */
    public static int getPointer() {
        return pointer.get();
    }

    /**
     * Reset the pointer index to 0
     */
    private static void resetPointer() {
        pointer.set(0);
    }

    /**
     * Get the logger with the current project name. Creates a new logger with every
     * name change.
     * 
     * @return The current logger
     * @see #NAME
     * @see #logger
     * @see Logger
     */
    private static Logger getLogger() {
        if (logger.get() == null) {
            logger.set(LoggerFactory.getLogger(NAME.get()));
        }
        return logger.get();
    }

    /**
     * Send a user input.
     * @param input {@link String} input
     */
    public static void sendInput(String input) {
        inputLogger.info(input);
        PRINT_LISTENERS.forEach(consumer -> consumer.accept("INPUT", input));
        INPUT_LISTENERS.forEach(consumer -> consumer.accept(input));
        INPUT_LISTENERS.clear();
    }

    /**
     * Send a print statement to the runtime console.
     * 
     * @param message Message to send (arguments can be inserted by adding "{}" in
     *                the message)
     * @param args    Arguments to append in the message
     * @see #getLogger()
     */
    public static void print(Object message, Object... args) {
        getLogger().info(message.toString(), args);
        PRINT_LISTENERS.forEach(consumer -> consumer.accept(getLogger().getName(), formatMessage(message, args)));
    }

    /**
     * Send a print statement to the runtime console.
     * 
     * @param message Message to send (arguments can be inserted by adding "{}" in
     *                the message)
     * @param args    Arguments to append in the message
     * @see #getLogger()
     */
    public static void warn(Object message, Object... args) {
        getLogger().warn(message.toString(), args);
        WARN_LISTENERS.forEach(consumer -> consumer.accept(getLogger().getName(), formatMessage(message, args)));
    }

    /**
     * Send an error statement to the development console.
     * 
     * @param error Message to send (arguments can be inserted by adding "{}" in the
     *              message).
     * @see #print(Object, Object...)
     * @see #error(Object, Exception)
     * @see #warn(Object, Object...)
     */
    public static void error(Object error) {
        getLogger().error(error.toString());
        ERROR_LISTENERS.forEach(consumer -> consumer.accept(getLogger().getName(), formatMessage(error.toString())));
    }

    /**
     * Send an error statement and stack trace to the development console.
     * 
     * @param error Error to send
     * @param e     Exception to send (used to print stack trace)
     * @see #print(Object, Object...)
     * @see #error(Object)
     * @see #warn(Object, Object...)
     */
    public static void error(Object error, Exception e) {
        getLogger().error(error.toString(), e);
        ERROR_LISTENERS.forEach(consumer -> consumer.accept(getLogger().getName(),
                formatMessage(error.toString()) + "\n" + e.getMessage()));
    }

    /**
     * Format a message by replacing instances of {@code "{}"} with a corresponding
     * argument
     * 
     * @param message Message
     * @param args    Arguments
     * @return Formatted string
     * @see MessageFormatter#format(String, Object)
     */
    protected static String formatMessage(Object message, Object... args) {
        return MessageFormatter.arrayFormat(String.valueOf(message), args).getMessage();
    }

    /**
     * Add a print listener
     * 
     * @param consumer Consumer to run when something is printed
     * @see #print(Object, Object...)
     */
    public static void addPrintListener(BiConsumer<String, String> consumer) {
        PRINT_LISTENERS.add(consumer);
    }

    /**
     * Add a warn listener
     * 
     * @param consumer Consumer to run when something is warned
     * @see #warn(Object, Object...)
     */
    public static void addWarnListener(BiConsumer<String, String> consumer) {
        WARN_LISTENERS.add(consumer);
    }

    /**
     * Add a error listener
     * 
     * @param consumer Consumer to run when something is errored
     * @see #warn(Object, Object...)
     */
    public static void addErrorListener(BiConsumer<String, String> consumer) {
        ERROR_LISTENERS.add(consumer);
    }

    /**
     * Add a variable listener. This listener is triggered when the variable with the corresponding name is changed.
     * @param name Variable name
     * @param consumer The consumer to run
     * @see #setVariable(String, Object)
     */
    public static void addVariableListener(String name, Consumer<Object> consumer) {
        VARIABLE_LISTENERS.put(name, consumer);
    }

    /**
     * Add an input subscription. Consumers are removed from the subscription list after they are run.
     * @param consumer Consumer to run when user inputs something
     */
    public static void addInputSubscription(Consumer<String> consumer) {
        INPUT_LISTENERS.add(consumer);
    }
}
