package com.wyu4.snowberryjam.Compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
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
    private static final Logger runtimeLogger = LoggerFactory.getLogger("Snowberry Jam");

    private static JsonNode getTree(String source) throws JsonProcessingException {
        print("Creating tree...");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(source);
    }

    public static void compile(String source) throws JsonProcessingException {
        flush();
        JsonNode tree = getTree(source);
        JsonNode projectBody = tree.get(SourceKey.BODY.toString());
        mapProjectVariables(projectBody);
        compileEvents(projectBody);
    }

    private static void mapProjectVariables(JsonNode projectBody) {
        JsonNode projectVariables = projectBody.get(SourceKey.VARIABLES.toString());
        print("Mapping variables...");
        projectVariables.elements().forEachRemaining((projectVariable) -> {
            String variableName = projectVariable.get(SourceKey.NAME.toString()).asText();
            JsonNode valueNode = projectVariable.get(SourceKey.VALUE.toString());
            Object rawNode;

            if (valueNode == null) {
                return;
            }

            if (valueNode.isInt() || valueNode.isDouble()) {
                rawNode = valueNode.asDouble();
                printTab("Number {} = {}", variableName, valueNode);
            } else if (valueNode.isBoolean()) {
                rawNode = valueNode.asBoolean();
                printTab("Boolean {} = {}", variableName, valueNode);
            } else if (valueNode.isTextual()) {
                rawNode = valueNode.asText();
                printTab("String {} = \"{}\"", variableName, valueNode);
            } else {
                return;
            }

            try {
                createVariable(variableName, rawNode);
            } catch (IllegalStateException e) {
                error("Could not create variable.", e);
            }
        });
    }

    private static void compileEvents(JsonNode projectBody) {
        JsonNode projectEvents = projectBody.get(SourceKey.EVENTS.toString());
        projectEvents.elements().forEachRemaining((eventNode) -> {
            String eventId = eventNode.get(SourceKey.ID.toString()).asText();
            if (eventId.equals(SourceId.ON_RUN.toString())) {
                BodyStack onRunBody = new BodyStack(SourceId.ON_RUN);
                compileBody(eventNode.get(SourceKey.BODY.toString()), onRunBody);
                stackAdd(onRunBody);

                System.out.print("\n\n------------------------------------\n------------------------------------\n\n");
            }
        });
    }

    private static void compileBody(JsonNode body, BodyStack stack) {
        body.elements().forEachRemaining((node) -> {
            String id = node.get(SourceKey.ID.toString()).asText();
            if (SourceId.PRINT.toString().equals(id)) {
                JsonNode messageNode = node.get(SourceKey.VALUE.toString());
                PrintTask task;
                if (messageNode.isTextual()) {
                    task = new PrintTask(messageNode.asText());
                } else {
                    task = new PrintTask(createVariableReference(messageNode));
                }
                stack.addElement(task);
            }
        });
    }

    private static VariableReference<?> createVariableReference(JsonNode node) {
        if (!node.get(SourceKey.ID.toString()).asText().equals(SourceId.VARIABLE.toString())) {
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

    public static void print(String message, Object... args) {
        logger.info(message, args);
    }

    public static void printTab(String message, Object... args) {
        message = "\t\t" + message;
        print(message, args);
    }

    public static void error(String error) {
        logger.error(error);
    }

    public static void error(String error, Exception e) {
        logger.error(error, e);
    }

    public static void main(String[] args) {
        File sourceFile = new File("concept.snowb");
        Scanner reader = null;
        try {
            reader = new Scanner(sourceFile);
            StringBuilder source = new StringBuilder("{");
            while(reader.hasNextLine()) {
                source.append(reader.nextLine().replaceAll("\t",""));
            }
            source.append("}");
            reader.close();

            compile(source.toString());
        } catch (Exception e) {
            logger.error("Main error.", e);
            if (reader != null) {
                reader.close();
            }
        }
        runStack();
    }
}
