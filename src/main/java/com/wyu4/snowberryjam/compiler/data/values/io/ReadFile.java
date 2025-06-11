package com.wyu4.snowberryjam.compiler.data.values.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.ResourceUtils;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;

import java.io.File;

public class ReadFile extends InteractiveValueHolder {

    public ReadFile(JsonNode node) {
        super(node);
    }

    public ReadFile(ValueHolder a) {
        super(a, new ValueHolder());
    }

    @Override
    public Object getValue() {
        String name = getA().getString();
        File file = new File(name);
        if (file.exists()) {
            try {
                return ResourceUtils.readFile(file);
            } catch (Exception e) {
                throw new RuntimeException("Cannot read file. " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Cannot read file. File \"%s\" does not exist.".formatted(name));
        }
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }
}
