package com.btm.planb.diffobject.generate.processor;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.processor.sub.AnnotationParseProcessor;
import com.btm.planb.diffobject.generate.processor.sub.MethodGenerateProcessor;
import com.btm.planb.diffobject.generate.processor.sub.ImportGenerateProcessor;
import com.btm.planb.diffobject.generate.processor.sub.JavaWriteProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 子处理器管理器
 */
public class SubProcessorManager {

    private static final List<AbstractSubProcessor> subProcessor = new ArrayList<>();

    public SubProcessorManager() {
        // 解析注解信息
        subProcessor.add(new AnnotationParseProcessor());
        // 收集import信息，并生成import信息的java文件的语句
        subProcessor.add(new ImportGenerateProcessor());
        // 根据解析的方法元信息，生成方法的java文件的语句
        subProcessor.add(new MethodGenerateProcessor());
        // 生成Java文件
        subProcessor.add(new JavaWriteProcessor());
    }

    public void process(ProcessContext processContext) {
        subProcessor.forEach(processor -> processor.process(processContext));
    }
}
