package com.wyu4.snowberryjam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public abstract class ResourceUtils {
    private static final Logger logger = LoggerFactory.getLogger("ResourceUtils");
    public static final String IMAGES = "images";

    public enum ResourceFile {
        COMPILER_ICON(IMAGES + "/CompilerIcon.png"),
        DEFAULT_SOURCE("DefaultSourceFile.snowb"),
        IMAGES_FOLDER(IMAGES),
        STYLE("style.css");

        private final String path;
        ResourceFile(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return path;
        }
    }

    public static String getFullPath(ResourceFile file) {
        return getFullPathAsUrl(file).toExternalForm();
    }

    public static URL getFullPathAsUrl(ResourceFile file) {
        return Objects.requireNonNull(ResourceUtils.class.getResource(file.toString()));
    }

    public static String readFile(ResourceFile file) {
        try {
            return readFile(new File(getFullPathAsUrl(file).toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(File file) {
        try (Scanner reader = new Scanner(file)) {
            StringBuilder source = new StringBuilder();
            while (reader.hasNextLine()) {
                source.append(reader.nextLine()).append("\n");
            }

            int lastIndex = source.lastIndexOf("\n");
            if (lastIndex != -1) {
                source.delete(lastIndex, lastIndex + 1);
            }

            return source.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
