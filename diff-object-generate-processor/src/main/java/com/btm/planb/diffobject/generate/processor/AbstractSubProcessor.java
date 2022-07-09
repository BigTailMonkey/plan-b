package com.btm.planb.diffobject.generate.processor;

import com.btm.planb.diffobject.generate.context.ProcessContext;

public abstract class AbstractSubProcessor {

    public void process(ProcessContext processContext) {
        this.doProcess(processContext);
    }

    public abstract void doProcess(ProcessContext processContext);

}
