package com.wyu4.snowberryjam.compiler;

import com.wyu4.snowberryjam.compiler.data.BodyStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Scanner;

public class CompilerTest {

    public static void test(File sourceFile) {
        final Logger logger = LoggerFactory.getLogger("Test");
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

    public static void main(String[] args) {
        BodyStack.setDebuggingEnabled(true);
        test(new File("example\\BubbleSort.snowb"));
    }
}
