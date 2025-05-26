package com.wyu4.snowberryjam.Framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interactor {
    private static final Logger logger = LoggerFactory.getLogger(Interactor.class);
    private final Model model;

    public Interactor(Model model) {
        this.model = model;
    }
}
