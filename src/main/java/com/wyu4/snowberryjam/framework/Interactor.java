package com.wyu4.snowberryjam.framework;

import com.wyu4.snowberryjam.ResourceUtils;
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

                    try {
                        LocalStorage.flush();

                        String source = ResourceUtils.readFile(file);

                        model.getSourceCodeProperty().set(source);
                        Compiler.print("Opened {}", file.getName());
                    } catch (Exception e) {
                        Compiler.error("Could not open file.", e);
                    }
                } else {
                    LocalStorage.warn("Cancelled file selection.");
                }
            });
        };
    }

    public Runnable createCompileTask() {
        return createCompileTask(() -> {});
    }

    public Runnable createCompileTask(Runnable callback) {
        return () -> {
            if (model.getCompiling()) {
                return;
            }

            new Thread(() -> {
                model.getCompilingProperty().setValue(true);
                try {
                    String sourceCode = model.getSourceCode();
                    Compiler.print("Compiling...");
                    Compiler.compile(sourceCode);
                    Compiler.print("Done.");
                    model.getBuiltSourceCodeProperty().set(sourceCode);
                    callback.run();
                } catch (Exception e) {
                    Compiler.error("Error compiling.", e);
                } finally {
                    model.getCompilingProperty().setValue(false);
                }
            }).start();
        };
    }

    public Runnable createRunTask() {
        return () -> {
            if (model.getRunning()) {
                return;
            }

            if (!model.getSourceCode().equals(model.getBuiltSourceCode())) {
                createCompileTask(createRunTask()).run();
            } else {
                model.getRunningProperty().setValue(true);
                new Thread(() -> {
                    try {
                        LocalStorage.runStack();
                    } catch (Exception e) {
                        LocalStorage.error("Error running.", e);
                    } finally {
                        model.getRunningProperty().setValue(false);
                    }
                }).start();
            }
        };
    }
}
