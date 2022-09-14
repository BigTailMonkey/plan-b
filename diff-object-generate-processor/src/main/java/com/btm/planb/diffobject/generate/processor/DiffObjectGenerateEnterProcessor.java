package com.btm.planb.diffobject.generate.processor;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.meta.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.*;

/**
 * 差异对象生成入口处理器
 */
public class DiffObjectGenerateEnterProcessor extends AbstractProcessor {

    private ProcessContext processContext;
    private SubProcessorManager subProcessorManager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processContext = new ProcessContext(processingEnv);
        subProcessorManager = new SubProcessorManager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.processContext.setRoundEnvironment(roundEnv);
        subProcessorManager.process(processContext);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Mappers.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
