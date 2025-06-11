package com.wyu4.snowberryjam;

import atlantafx.base.theme.NordLight;
import com.wyu4.snowberryjam.framework.Controller;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

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
            stage.getIcons().add(new Image(ResourceUtils.getFullPath(ResourceUtils.ResourceFile.COMPILER_ICON)));
        } catch (Exception e) {
            logger.error("Could not set app icon.", e);
        }

        Controller controller = new Controller(stage);
        try {
            List<String> args = getParameters().getRaw();
            if (!args.isEmpty() && args.getFirst() != null) {
                File sourceFile = new File(args.getFirst());
                logger.info("Opened: {}", sourceFile.getAbsolutePath());
                controller.updateFile(sourceFile);
            } else {
                controller.updateFile(null);
            }
        } catch (Exception e) {
            logger.error("Could not set default source code.", e);
        }

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        javafx.application.Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

        Consumer<File> updateTitle = file -> {
            if (file == null) {
                stage.setTitle("Snowberry Jam");
            } else {
                stage.setTitle("[" + file.getName() + "] - Snowberry Jam");
            }
        };
        updateTitle.accept(controller.getSourceFile());
        controller.getSourceFileProperty().addListener((evt, old, file) -> updateTitle.accept(file));
        stage.setMaximized(true);
        stage.setMinWidth(screenBounds.getWidth()*0.5);
        stage.setMinHeight(screenBounds.getHeight()*0.5);
        stage.setMaxWidth(screenBounds.getWidth());
        stage.setMaxHeight(screenBounds.getHeight());

        Scene primaryScene = new Scene(controller.getView());
        primaryScene.getStylesheets().add(ResourceUtils.getFullPath(ResourceUtils.ResourceFile.STYLE));
        stage.setScene(primaryScene);

        stage.show();
        stage.setOnCloseRequest(evt -> {
            logger.info("Window closing...");
            System.exit(0);
        });
    }
}
