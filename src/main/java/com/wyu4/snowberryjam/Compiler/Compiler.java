package com.wyu4.snowberryjam.Compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import com.wyu4.snowberryjam.Compiler.DataType.Tasks.PrintTask;
import com.wyu4.snowberryjam.Compiler.DataType.VariableReference;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            JsonNode rawValue = projectVariable.get(SourceKey.VALUE.toString());
            Object valueNode;

            if (rawValue == null) {
                return;
            }

            if (rawValue.isInt() || rawValue.isDouble()) {
                valueNode = rawValue.asDouble();
                printTab("Number {} = {}", variableName, valueNode);
            } else if (rawValue.isBoolean()) {
                valueNode = rawValue.asBoolean();
                printTab("Boolean {} = {}", variableName, valueNode);
            } else if (rawValue.isTextual()) {
                valueNode = rawValue.asText();
                printTab("String {} = \"{}\"", variableName, valueNode);
            } else {
                return;
            }

            try {
                createVariable(variableName, valueNode);
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
            }
        });
    }

    private static void compileBody(JsonNode body, BodyStack stack) {
        body.elements().forEachRemaining((node) -> {
            String id = node.get(SourceKey.ID.toString()).asText();
            if (SourceId.PRINT.toString().equals(id)) {
                JsonNode messageNode = node.get(SourceKey.VALUE.toString());
//                if (messageNode.isTextual()) {
//                }
                stack.addElement(new PrintTask(messageNode.textValue()));
            }
        });
    }

//    private static VariableReference<?> createVariableReference(JsonNode node) {
//
//    }

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
            StringBuilder source = new StringBuilder();
            while(reader.hasNextLine()) {
                source.append(reader.nextLine());
            }
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
