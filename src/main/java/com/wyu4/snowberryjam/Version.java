package com.wyu4.snowberryjam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Build information, read from version.properties (filled in by Maven from pom.xml).
 */
public final class Version {
    /**
     * The app version, taken from the {@code sjVersion} property in pom.xml.
     */
    public static final String VERSION = load();

    private Version() {}

    private static String load() {
        try (InputStream in = Version.class.getResourceAsStream("version.properties")) {
            if (in == null) {
                return "unknown";
            }
            Properties props = new Properties();
            props.load(in);
            return props.getProperty("version", "unknown");
        } catch (IOException e) {
            return "unknown";
        }
    }
}
