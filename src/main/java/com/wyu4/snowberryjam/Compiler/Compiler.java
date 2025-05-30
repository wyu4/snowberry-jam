package com.wyu4.snowberryjam.Compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.ExecutableTask;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.SetTask;
import com.wyu4.snowberryjam.Compiler.DataType.VariableReference;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wyu4.snowberryjam.Compiler.DataType.Tasks.PrintTask;

import java.io.File;
import java.util.Scanner;

public abstract class Compiler extends LocalStorage {
    private static final Logger logger = LoggerFactory.getLogger("Compiler");

    private static JsonNode getTree(String source) throws JsonProcessingException {
        print("Creating tree...");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(source);
    }

    public static void compile(String source) throws JsonProcessingException {
        JsonNode tree = getTree(source);
        mapProjectData(tree);
        JsonNode projectBody = tree.get(SourceKey.BODY.toString());
        mapProjectVariables(projectBody);
        compileEvents(projectBody);
    }

    private static void mapProjectData(JsonNode tree) {
        JsonNode nameNode = tree.get(SourceKey.NAME.toString());
        if (nameNode != null) {
            setName(nameNode.asText().replaceAll("\n", ""));
        }
    }

    private static void mapProjectVariables(JsonNode projectBody) {
        JsonNode projectVariables = projectBody.get(SourceKey.VARIABLES.toString());
        print("Mapping variables...");
        projectVariables.elements().forEachRemaining((projectVariable) -> {
            String variableName = projectVariable.get(SourceKey.NAME.toString()).asText();
            JsonNode valueNode = projectVariable.get(SourceKey.VALUE.toString());
            if (valueNode == null) {
                return;
            }

            Object rawValue = asObject(valueNode);
            printTab("VARIABLE \"{}\" -> {}", variableName, rawValue);

            try {
                createVariable(variableName, rawValue);
            } catch (IllegalStateException e) {
                error("Could not create variable.", e);
            }
        });
    }

    private static void compileEvents(JsonNode projectBody) {
        JsonNode projectEvents = projectBody.get(SourceKey.EVENTS.toString());
        projectEvents.elements().forEachRemaining((eventNode) -> {
            if (sameId(eventNode, SourceId.ON_RUN)) {
                BodyStack onRunBody = new BodyStack(SourceId.ON_RUN);
                compileBody(eventNode.get(SourceKey.BODY.toString()), onRunBody);
                stackAdd(onRunBody);

                System.out.print("\n\n------------------------------------\n------------------------------------\n\n");
            }
        });
    }

    private static void compileBody(JsonNode body, BodyStack stack) {
        body.elements().forEachRemaining((node) -> {
            ExecutableTask task = null;

            // Print
            if (sameId(node, SourceId.PRINT)) {
                JsonNode messageNode = node.get(SourceKey.VALUE.toString());
                if (messageNode.isTextual()) {
                    task = new PrintTask(messageNode.asText());
                } else {
                    task = new PrintTask(createVariableReference(messageNode));
                }

            } else if (sameId(node, SourceId.SET)) {
                String name = getName(node);
                JsonNode valueNode = node.get(SourceKey.VALUE.toString());
                if (isValue(valueNode)) {
                    task = new SetTask(name, asObject(valueNode));
                } else {
                    task = new SetTask(name, createVariableReference(valueNode));
                }
            }

            stack.addElement(task);
        });
    }

    private static String getProperty(JsonNode node, String key) {
        JsonNode keyNode = node.get(key);
        if (keyNode == null) {
            return null;
        }
        return keyNode.asText();
    }

    public static String getId(JsonNode node) {
        return getProperty(node, SourceKey.ID.toString());
    }

    public static String getName(JsonNode node) {
        return getProperty(node, SourceKey.NAME.toString());
    }

    public static boolean sameId(JsonNode node, SourceId id) {
        return id.toString().equals(getId(node));
    }

    public static boolean isValue(JsonNode node) {
        return node.isTextual() || node.isBoolean() || node.isNumber();
    }

    public static Object asObject(JsonNode node) {
        if (node.isNumber()) {
            return node.asDouble();
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        }
        return null;
    }

    private static VariableReference<?> createVariableReference(JsonNode node) {
        if (!sameId(node, SourceId.VARIABLE)) {
            throw new ClassCastException(node + " is not a variable reference.");
        }

        JsonNode nameNode = node.get(SourceKey.NAME.toString());
        if (nameNode.isTextual()) {
            String name = nameNode.asText();
            Object defaultValue = LocalStorage.getRaw(name);
            return new VariableReference<>(name, defaultValue.getClass());
        }

        return new VariableReference<Object>(node.get(SourceKey.NAME.toString()).asText(), Object.class);
    }

    public static void print(Object message, Object... args) {
        logger.info(message.toString(), args);
    }

    public static void printTab(Object message, Object... args) {
        message = "\t\t" + message.toString();
        print(message, args);
    }

    public static void error(Object error) {
        logger.error(error.toString());
    }

    public static void error(Object error, Exception e) {
        logger.error(error.toString(), e);
    }
}
