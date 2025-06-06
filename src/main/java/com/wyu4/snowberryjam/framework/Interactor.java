package com.wyu4.snowberryjam.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The interactor of the MVCI framework.
 */
public class Interactor {
    private static final Logger logger = LoggerFactory.getLogger(Interactor.class);
    private final Model model;

    /**
     * Create a new Interactor
     * @param model The model of the MVCI framework
     */
    public Interactor(Model model) {
        this.model = model;
    }
}
