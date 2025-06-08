module com.wyu4.snowberryjam {
    // JavaFX
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.base;
    requires transitive atlantafx.base;

    // RichTextFX
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires org.fxmisc.wellbehaved;

    // Logging
    requires org.apache.logging.log4j;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires java.logging;

    // Ikonli
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;

    // Other
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires java.compiler;

    exports com.wyu4.snowberryjam;
}