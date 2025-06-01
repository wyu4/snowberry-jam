package com.wyu4.snowberryjam;

import atlantafx.base.theme.NordLight;
import com.wyu4.snowberryjam.Compiler.CompilerTest;
import com.wyu4.snowberryjam.Framework.Controller;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Main entrypoint for the JavaFX launcher
 */
public class Start extends Application {
    private static final Logger logger = LoggerFactory.getLogger("Launcher");

    public static void main(String[] args) {
        System.out.println("Launching Snowberry Jam");
        launch(args);
    }

    /**
     * Creates a new Snowberry Jam process
     * @param stage JavaFX Stage
     * @throws Exception Exceptions that arise during the setup
     */
    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        javafx.application.Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

        stage.setTitle("Snowberry Jam");
        stage.setMinWidth(screenBounds.getWidth()/2);
        stage.setMinHeight(screenBounds.getHeight()/2);
        stage.setMaxWidth(screenBounds.getWidth());
        stage.setMaxHeight(screenBounds.getHeight());

        Scene primaryScene = new Scene(new Controller().getView());
        stage.setScene(primaryScene);

        stage.show();
        stage.setOnCloseRequest(evt -> {
            logger.info("Window closing...");
            System.exit(0);
        });

        File sourceFile = null;

        List<String> args = getParameters().getRaw();
        if (!args.isEmpty() && args.getFirst() != null) {
            sourceFile = new File(args.getFirst());
            logger.info("Opened: {}", sourceFile.getAbsolutePath());
        } else {
            logger.info("Please open a file: ");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a Snowberry Jam Source File to run");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Snowberry Jam Source File (*.snowb)", "*.snowb"));
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                sourceFile = file;
                logger.info("File opened from chooser: {}", sourceFile.getAbsolutePath());
            } else {
                logger.info("No file selected.");
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                }).start();
            }
        }
        if (sourceFile != null) {
            CompilerTest.test(sourceFile);
        }
    }
}
