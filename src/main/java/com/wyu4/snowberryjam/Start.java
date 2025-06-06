package com.wyu4.snowberryjam;

import atlantafx.base.theme.NordLight;
import com.wyu4.snowberryjam.compiler.CompilerTest;
import com.wyu4.snowberryjam.framework.Controller;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * The main entry point.
 */
public class Start extends Application {
    private static final Logger logger = LoggerFactory.getLogger("Launcher");

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts a new Snowberry Jam process
     * @param stage JavaFX Stage
     * @throws Exception Exceptions that arise during the setup
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.getIcons().add(new Image(ResourceUtils.getFullPath(ResourceUtils.ResourceFile.CompilerIcon)));
        } catch (Exception e) {
            logger.error("Could not set app icon.", e);
        }

        Controller controller;

        List<String> args = getParameters().getRaw();
        if (!args.isEmpty() && args.getFirst() != null) {
            File sourceFile = new File(args.getFirst());
            logger.info("Opened: {}", sourceFile.getAbsolutePath());
            controller = new Controller(stage, sourceFile);
        } else {
            controller = new Controller(stage);
        }

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        javafx.application.Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

        stage.setTitle("Snowberry Jam");
        stage.setMaximized(true);
        stage.setMinWidth(screenBounds.getWidth()*0.5);
        stage.setMinHeight(screenBounds.getHeight()*0.5);
        stage.setMaxWidth(screenBounds.getWidth());
        stage.setMaxHeight(screenBounds.getHeight());

        Scene primaryScene = new Scene(controller.getView());
        stage.setScene(primaryScene);

        stage.show();
        stage.setOnCloseRequest(evt -> {
            logger.info("Window closing...");
            System.exit(0);
        });

//        File sourceFile = null;
//
//        List<String> args = getParameters().getRaw();
//        if (!args.isEmpty() && args.getFirst() != null) {
//            sourceFile = new File(args.getFirst());
//            logger.info("Opened: {}", sourceFile.getAbsolutePath());
//        } else {
//            logger.info("Please open a file: ");
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Select a Snowberry Jam Source File to run");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Snowberry Jam Source File (*.snowb)", "*.snowb"));
//            File file = fileChooser.showOpenDialog(stage);
//
//            if (file != null) {
//                sourceFile = file;
//                logger.info("File opened from chooser: {}", sourceFile.getAbsolutePath());
//            } else {
//                logger.info("No file selected.");
//                new Thread(() -> {
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.exit(0);
//                }).start();
//            }
//        }
//        if (sourceFile != null) {
//            CompilerTest.test(sourceFile);
//        }
    }
}
