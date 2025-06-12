package com.wyu4.snowberryjam.compiler.data.tasks.macros;

import com.fasterxml.jackson.databind.JsonNode;
import com.wyu4.snowberryjam.compiler.LocalStorage;
import com.wyu4.snowberryjam.compiler.data.tasks.SetTask;
import com.wyu4.snowberryjam.compiler.data.values.ValueHolder;
import com.wyu4.snowberryjam.compiler.data.values.builtin.InputHolder;
import com.wyu4.snowberryjam.compiler.enums.SourceKey;

/**
 * A macro that sets a variable to a user input. The name of the variable is stored as
 * {@link SourceKey#NAME}.
 */
public class InputMacro extends SetTask {

    /**
     * Create a new set statement
     * 
     * @param node The {@link JsonNode} to refer
     */
    public InputMacro(JsonNode node) {
        this(ValueHolder.fromNode(node.get(SourceKey.NAME.toString())));
    }

    /**
     * Create a new set statement
     * 
     * @param name The name of the variable to set
     */
    public InputMacro(ValueHolder name) {
        super(name, new InputHolder());
    }

    @Override
    public void execute() {
        if (!LocalStorage.isRunning()) {
            return;
        }
        
        if (getNameValue().notEmpty()) {
            super.execute();
        } else {
            feedback();
        }
    }
}
