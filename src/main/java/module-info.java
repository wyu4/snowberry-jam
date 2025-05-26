module com.wyu4.snowberryjam {
    requires org.apache.logging.log4j;
    requires ch.qos.logback.classic;
    requires java.desktop;
    requires org.slf4j;
    requires java.logging;
    requires com.fasterxml.jackson.databind;
    requires java.base;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;

    exports com.wyu4.snowberryjam;
}