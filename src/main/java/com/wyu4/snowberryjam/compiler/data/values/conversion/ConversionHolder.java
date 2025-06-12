package com.wyu4.snowberryjam.compiler.data.values.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.data.values.InteractiveValueHolder;

public class ConversionHolder extends InteractiveValueHolder {
    public ConversionHolder(JsonNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return "default unset conditional holder with parameters %s and %s".formatted(getA(), getB());
    }
}
