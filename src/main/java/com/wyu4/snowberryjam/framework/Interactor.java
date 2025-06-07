package com.wyu4.snowberryjam.framework;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;

import java.io.File;
import java.util.Scanner;

/**
 * The interactor of the MVCI framework.
 */
public class Interactor {
    private static final Logger logger = LoggerFactory.getLogger(Interactor.class);
    private final Model model;
    private final Stage stage;

    /**
     * Create a new Interactor
     * 
     * @param model The {@link Model} of the MVCI framework
     * @param stage The primary {@link Stage} of the window
     */
    public Interactor(Model model, Stage stage) {
        this.model = model;
        this.stage = stage;
    }

    public Runnable createOpenFileTask() {
        return () -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open with Snowberry Jam");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Snowberry Jam Source File (*.snowb)", "*.snowb"));
            Platform.runLater(() -> {
                File file = fileChooser.showOpenDialog(stage);

                if (file != null) {
                    model.getSourceFileProperty().set(file);
                    Compiler.print("Opened {}", file.getName());
                    createCompileTask().run();
                } else {
                    LocalStorage.warn("Cancelled file selection.");
                }
            });
        };
    }

    public Runnable createCompileTask() {
        return () -> {
            File sourceFile = model.getSourceFile();

            if (sourceFile == null) {
                Compiler.error("Source file not found.");
                return;
            }

            Compiler.print("Reading...");
            Scanner reader = null;
            try {
                LocalStorage.flush();

                reader = new Scanner(sourceFile);
                StringBuilder source = new StringBuilder();
                while (reader.hasNextLine()) {
                    source.append(reader.nextLine().replaceAll("\t", ""));
                }
                reader.close();

                Compiler.print("Compiling...");
                Compiler.compile(source.toString());
                Compiler.print("Done.");
            } catch (Exception e) {
                Compiler.error("Error compiling.", e);
                if (reader != null) {
                    reader.close();
                }
            }
        };
    }
}
