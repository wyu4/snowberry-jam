package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

public interface ExecutableTask {
    void execute();
    default Object feedback() {
        return null;
    };
}
