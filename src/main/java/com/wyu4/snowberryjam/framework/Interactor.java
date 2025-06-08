package com.wyu4.snowberryjam.framework;

import com.wyu4.snowberryjam.ResourceUtils;
import com.wyu4.snowberryjam.compiler.Compiler;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

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

        Consumer<File> updateItems = file -> {
            Platform.runLater(() -> {
                model.getSaveDisabledProperty().set(file == null);
            });
        };
        updateItems.accept(model.getSourceFile());
        model.getSourceFileProperty().addListener((evt, old, file) -> {
            updateItems.accept(file);
        });
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
                    createSetFileTask(file).run();
                } else {
                    Compiler.warn("Cancelled file selection.");
                }
            });
        };
    }

    public Runnable createSetFileTask(File file) {
        return () -> {
            model.getSourceFileProperty().set(file);

            try {
                model.getSourceCodeProperty().set(ResourceUtils.readFile(file));
                Compiler.print("Opened \"{}\"", file.getName());
            } catch (Exception e) {
                Compiler.error("Could not open file.", e);
            }
        };
    }

    public Runnable createSaveFileTask() {
        return createSaveFileTask(model.getSourceFile());
    }

    public Runnable createSaveFileTask(File file) {
        return () -> {
            if (file == null) {
                createSaveAsFileTask().run();
                return;
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(model.getSourceCode());
                Compiler.print("Saved to \"{}\"", file.getName());
            } catch (Exception e) {
                Compiler.error("Could not save file.", e);
            }
        };
    }

    public Runnable createSaveAsFileTask() {
        return () -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Snowberry Jam Source File (*.snowb)", "*.snowb"));
            Platform.runLater(() -> {
                File file = fileChooser.showSaveDialog(stage);

                if (file == null) {
                    Compiler.warn("Saving cancelled.");
                    return;
                }

                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException e) {
                    Compiler.error("Could not create file \"%s\"".formatted(file.getName()), e);
                    return;
                }

                createSaveFileTask(file).run();
                createSetFileTask(file).run();
            });
        };
    }

    public Runnable createNewProjectTask() {
        return () -> {
            Platform.runLater(() -> {
                model.getSourceFileProperty().set(null);
                model.getSourceCodeProperty().set(LocalStorage.getDefaultSource());
                Compiler.print("Created new project.");
            });
        };
    }

    public Runnable createCompileTask() {
        return createCompileTask(() -> {
        });
    }

    public Runnable createCompileTask(Runnable callback) {
        return () -> {
            if (model.getCompiling()) {
                return;
            }

            new Thread(() -> {
                model.getCompilingProperty().setValue(true);
                try {
                    LocalStorage.flush();
                    Compiler.print("Compiling...");
                    String sourceCode = model.getSourceCode();
                    Compiler.compile(sourceCode);
                    Compiler.print("Done.");
                    model.getSourceCodeProperty().set(sourceCode);
                    model.getBuiltSourceCodeProperty().set(sourceCode);
                    callback.run();
                } catch (Exception e) {
                    Compiler.error("Error compiling:", e);
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

    public Runnable createFormatCodeTask() {
        return () -> {
            model.getSourceCodeProperty().set(Compiler.formatString(model.getSourceCode()));
        };
    }
}
