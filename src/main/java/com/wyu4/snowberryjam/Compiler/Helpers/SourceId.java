package com.wyu4.snowberryjam.Compiler.Helpers;

public enum SourceId {
    PROJECT("PROJECT");

    private final String id;
    SourceId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
