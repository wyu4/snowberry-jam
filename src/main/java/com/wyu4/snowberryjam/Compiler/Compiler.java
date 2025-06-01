package com.wyu4.snowberryjam.Compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.*;
import com.wyu4.snowberryjam.Compiler.Helpers.EnumHelper;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

            Object rawValue = asPrimitiveObject(valueNode);
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
            if (SourceId.ON_RUN.toString().equals(getId(eventNode))) {
                BodyStack onRunBody = new BodyStack(SourceId.ON_RUN);
                compileBody(eventNode.get(SourceKey.BODY.toString()), onRunBody);
                stackAdd(onRunBody);

                System.out.print("\n\n------------------------------------\n------------------------------------\n\n");
            }
        });
    }

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
                default -> {
                    warn("Task with ID \"{}\" is unrecognized. Skipped.", id.toString());
                    return;
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

    public static boolean isPrimitive(JsonNode node) {
        return node.isTextual() || node.isBoolean() || node.isNumber() || node.isArray();
    }

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

    public static void warn(Object message, Object... args) {
        logger.warn(message.toString(), args);
    }
}
