package com.wyu4.snowberryjam.Compiler.DataType;

import com.wyu4.snowberryjam.Compiler.DataType.Values.ValueHolder;
import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

/**
 * An element with a set of instruction
 */
public class CoreElement {
    /**
     * The source ID of the element
     */
    private final SourceId id;
    /**
     * The name of the element
     */
    private final ValueHolder name;

    /**
     * Create a new core element object
     * @param id The ID of the core element
     * @see SourceId
     */
    public CoreElement(SourceId id) {
        this(id, new ValueHolder(null));
    }

    /**
     * Create a new core element object
     * @param id The ID of the core element
     * @param name The name of the element. Depending on the type of element, it might affect how values are handled.
     * @see SourceId
     */
    public CoreElement(SourceId id, ValueHolder name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the ID tied to this element
     * @return The corresponding {@link SourceId}
     */
    public SourceId getId() {
        return id;
    }

    /**
     * Get the name of this element
     * @return Name
     */
    public String getName() {
        return name.getString();
    }
}
