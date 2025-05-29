package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;
import org.apache.logging.log4j.core.Core;

import java.util.ArrayList;
import java.util.List;

public class BodyStack extends ValidCoreElement {
    private final List<CoreElement> stack = new ArrayList<>();

    public BodyStack(SourceId id) {
        super(id);
    }

    public void addElement(CoreElement element) {
        stack.add(element);
    }

    public CoreElement[] copyStack() {
        CoreElement[] array = new CoreElement[stack.size()];
        stack.toArray(array);
        return array;
    }

    public void flush() {
        stack.clear();
    }
}
