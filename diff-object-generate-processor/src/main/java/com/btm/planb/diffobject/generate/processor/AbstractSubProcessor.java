package com.btm.planb.diffobject.generate.processor;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.util.RoundEnvironmentUtil;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 差异对象构建子处理器的抽象
 */
public abstract class AbstractSubProcessor {

    protected RoundEnvironment roundEnv;
    protected Messager messager;
    protected Filer filer;
    protected Elements elementUtil;
    protected Types typeUtil;
    protected RoundEnvironmentUtil roundEnvUtil;

    public void process(ProcessContext processContext) {
        this.roundEnv = processContext.getRoundEnvironment();
        this.messager = processContext.getMessager();
        this.filer = processContext.getFiler();
        this.elementUtil = processContext.getElementUtil();
        this.typeUtil = processContext.getTypeUtil();
        this.roundEnvUtil = new RoundEnvironmentUtil(this.typeUtil);
        this.doProcess(processContext);
    }

    public abstract void doProcess(ProcessContext processContext);

}
