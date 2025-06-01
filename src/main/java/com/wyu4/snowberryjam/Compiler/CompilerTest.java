package com.wyu4.snowberryjam.Compiler;

import com.wyu4.snowberryjam.Compiler.DataType.BodyStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Scanner;

public class CompilerTest {

    public static void main(String[] args) {
        BodyStack.setDebuggingEnabled(false);
        final Logger logger = LoggerFactory.getLogger("Test");

        File sourceFile = new File("Examples\\BubbleSort.snowb");
        Scanner reader = null;
        try {
            LocalStorage.flush();

            reader = new Scanner(sourceFile);
            StringBuilder source = new StringBuilder();
            while(reader.hasNextLine()) {
                source.append(reader.nextLine().replaceAll("\t",""));
            }
            reader.close();

            Compiler.compile(source.toString());
        } catch (Exception e) {
            logger.error("Main error.", e);
            if (reader != null) {
                reader.close();
            }
        }

        LocalStorage.runStack();
    }
}
