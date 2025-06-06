package com.wyu4.snowberryjam.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyu4.snowberryjam.compiler.data.BodyStack;
import com.wyu4.snowberryjam.compiler.data.tasks.*;
import com.wyu4.snowberryjam.compiler.enums.EnumHelper;
import com.wyu4.snowberryjam.compiler.enums.SourceId;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Snowberry Jam compiler class
 */
public abstract class Compiler extends LocalStorage {
    private static final Logger logger = LoggerFactory.getLogger("Compiler");

    /**
     * Creates a {@link JsonNode} tree from a JSON {@link String}
     * @param source JSON source code as a string
     * @return A JsonNode representation of the string
     * @throws JsonProcessingException Any exceptions that arise while mapping the contents of the string
     * @see #compile(String)
     */
    private static JsonNode getTree(String source) throws JsonProcessingException {
        print("Creating tree...");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(source);
    }

    /**
     * Compile the JSON contents of a {@link String}. Starts by creating a tree, maps project data, maps variables, and then compiles events.
     * @param source JSON source code as a string
     * @throws JsonProcessingException Any exceptions that arise while mapping the contents of the string
     * @see #getTree(String)
     * @see #mapProjectData(JsonNode)
     * @see #mapProjectVariables(JsonNode) 
     * @see #compileEvents(JsonNode) 
     */
    public static void compile(String source) throws JsonProcessingException {
        JsonNode tree = getTree(source);
        mapProjectData(tree);
        JsonNode projectBody = tree.get(SourceKey.BODY.toString());
        mapProjectVariables(projectBody);
        compileEvents(projectBody);
    }

    /**
     * Map the project data. THis includes any metadata such as the project name and description.
     * @param tree The full project tree
     */
    private static void mapProjectData(JsonNode tree) {
        JsonNode nameNode = tree.get(SourceKey.NAME.toString());
        if (nameNode != null) {
            setName(nameNode.asText().replaceAll("\n", ""));
        }
    }

    /**
     * Maps/stores all the variables found in the project's body. This only includes variable initialization in the {@code "variables":[]} section. This uses protected methods in {@link LocalStorage}
     * @param projectBody The body of the project
     * @see #createVariable(String, Object)
     */
    private static void mapProjectVariables(JsonNode projectBody) {
        JsonNode projectVariables = projectBody.get(SourceKey.VARIABLES.toString());
        print("Mapping variables...");
        projectVariables.elements().forEachRemaining((projectVariable) -> {
            String variableName = projectVariable.get(SourceKey.NAME.toString()).asText();
            JsonNode valueNode = projectVariable.get(SourceKey.VALUE.toString());
            if (valueNode == null) {
                return;
            }

            Object rawValue = asPrimitiveObject(valueNode);
            printTab("VARIABLE \"{}\" -> {}", variableName, rawValue);

            try {
                createVariable(variableName, rawValue);
            } catch (IllegalStateException e) {
                error("Could not create variable.", e);
            }
        });
    }

    /**
     * Compiles and stores all the events found in the {@code "events":[]} section in the project body to stacks, and subsequently converts compiles bodies to stacks. This uses protected methods in {@link LocalStorage}
     * @param projectBody The body of the project
     * @see #stackAdd(ExecutableTask)
     * @see #compileBody(JsonNode, BodyStack)
     */
    private static void compileEvents(JsonNode projectBody) {
        JsonNode projectEvents = projectBody.get(SourceKey.EVENTS.toString());
        projectEvents.elements().forEachRemaining((eventNode) -> {
            if (SourceId.ON_RUN.toString().equals(getId(eventNode))) {
                BodyStack onRunBody = new BodyStack(SourceId.ON_RUN);
                compileBody(eventNode.get(SourceKey.BODY.toString()), onRunBody);
                stackAdd(onRunBody);

                System.out.print("\n\n------------------------------------\n------------------------------------\n\n");
            }
        });
    }

    /**
     * Converts the contents of any {@code "body":[]} property into tasks, and stores them in a {@link BodyStack}.
     * @param body The selected body to compile
     * @param stack The stack to store compile tasks
     * @see BodyStack
     * @see ExecutableTask
     */
    public static void compileBody(JsonNode body, BodyStack stack) {
        if (body == null) { // Skip if body isn't found. Empty stack.
            return;
        }
        body.elements().forEachRemaining((node) -> {
            ExecutableTask task;

            SourceId id = EnumHelper.stringToId(getId(node));
            if (id == null) {
                return;
            }

            switch (id) {
                case PRINT -> task = new PrintTask(node);
                case SET -> task = new SetTask(node);
                case IF -> task = new IfTask(node);
                case IF_ELSE -> task = new IfElseTask(node);
                case WHILE -> task = new WhileTask(node);
                case REPEAT -> task = new Repeat(node);
                default -> {
                    warn("Task with ID \"{}\" is unrecognized. Skipped.", id.toString());
                    return;
                }
            }

            stack.addTask(task);
        });
    }

    /**
     * Grabs the value of a property given the key. Used by more specific methods.
     * @param node A node
     * @param key The key
     * @return The value of the property as a string. May be {@code null} if the property isn't found.
     * @see #getId(JsonNode)
     * @see #getName(JsonNode)
     */
    private static String getProperty(JsonNode node, String key) {
        JsonNode keyNode = node.get(key);
        if (keyNode == null) {
            return null;
        }
        return keyNode.asText();
    }

    /**
     * Grabs the ID of the node
     * @param node A node
     * @return The ID as a string. User helper methods to convert this into its respective {@link Enum}. May be {@code null} if the ID isn't found.
     * @see EnumHelper#stringToId(String)
     */
    public static String getId(JsonNode node) {
        return getProperty(node, SourceKey.ID.toString());
    }

    /**
     * Grabs the name of the node
     * @param node A node
     * @return The name as a string. May be {@code null} if the name isn't found.
     */
    public static String getName(JsonNode node) {
        return getProperty(node, SourceKey.NAME.toString());
    }

    /**
     * Get a node as a primitive object
     * @param node A node
     * @return A {@link Double}, {@link String}, {@link Boolean}, or {@code Array} parsed as an {@link Object}. Might return {@code null} if the node isn't primitive.
     */
    public static Object asPrimitiveObject(JsonNode node) {
        if (node.isNumber()) {
            return node.asDouble();
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isArray()) {
            Iterator<JsonNode> iterator = node.iterator();
            List<Object> list = new ArrayList<>();
            iterator.forEachRemaining((listNode) -> {
                list.add(asPrimitiveObject(listNode));
            });
            return list.toArray();
        }
        return null;
    }

    /**
     * Send a print statement to the development console.
     * @param message Message to send (arguments can be inserted by adding "{}" in the message).
     * @param args Arguments to append in the message
     * @see #printTab(Object, Object...)
     * @see #error(Object)
     * @see #error(Object, Exception)
     * @see #warn(Object, Object...)
     */
    public static void print(Object message, Object... args) {
        logger.info(message.toString(), args);
    }

    /**
     * Send a print statement to the development console. Messages send using this are inlined.
     * @param message Message to send (arguments can be inserted by adding "{}" in the message).
     * @param args Arguments to append in the message
     * @see #print(Object, Object...)
     * @see #error(Object)
     * @see #error(Object, Exception)
     * @see #warn(Object, Object...)
     */
    public static void printTab(Object message, Object... args) {
        message = "\t\t" + message.toString();
        print(message, args);
    }

    /**
     * Send an error statement to the development console.
     * @param error Message to send (arguments can be inserted by adding "{}" in the message).
     * @see #print(Object, Object...)
     * @see #printTab(Object, Object...)
     * @see #error(Object, Exception)
     * @see #warn(Object, Object...)
     */
    public static void error(Object error) {
        logger.error(error.toString());
    }

    /**
     * Send an error statement and stack trace to the development console.
     * @param error Error to send
     * @param e Exception to send (used to print stack trace)
     * @see #print(Object, Object...)
     * @see #printTab(Object, Object...)
     * @see #error(Object)
     * @see #warn(Object, Object...)
     */
    public static void error(Object error, Exception e) {
        logger.error(error.toString(), e);
    }

    /**
     * Send a warning statement to the development console.
     * @param message Warning to send (arguments can be inserted by adding "{}" in the message).
     * @param args Arguments to append in the message
     * @see #print(Object, Object...)
     * @see #printTab(Object, Object...)
     * @see #error(Object)
     * @see #error(Object, Exception)
     */
    public static void warn(Object message, Object... args) {
        logger.warn(message.toString(), args);
    }
}
