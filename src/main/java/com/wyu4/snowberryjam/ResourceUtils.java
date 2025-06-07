package com.wyu4.snowberryjam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class ResourceUtils {
    private static final Logger logger = LoggerFactory.getLogger("ResourceUtils");
    public static final String IMAGES = "images";

    public enum ResourceFile {
        COMPILER_ICON(IMAGES + "/CompilerIcon.png"),
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
        return Objects.requireNonNull(ResourceUtils.class.getResource(file.toString())).toExternalForm();
    }

    public static String getResource(ResourceFile file) {
        try (InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(file.toString())) {
            if (inputStream == null) {
                throw new IOException("File not found.");
            }

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                if (!result.isEmpty()) {
                    int lastNewLine = result.lastIndexOf("\n");
                    result.delete(lastNewLine, lastNewLine + 1);
                }

                return result.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
