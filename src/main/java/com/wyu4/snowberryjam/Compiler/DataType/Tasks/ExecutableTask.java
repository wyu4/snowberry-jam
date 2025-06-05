package com.wyu4.snowberryjam.Compiler.DataType.Tasks;

import com.wyu4.snowberryjam.Compiler.Helpers.SourceId;

public interface ExecutableTask {
    void execute();
    SourceId getId();
    default Object feedback() {
        return null;
    };
}
