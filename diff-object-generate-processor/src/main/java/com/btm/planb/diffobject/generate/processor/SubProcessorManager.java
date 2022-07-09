package com.btm.planb.diffobject.generate.processor;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.processor.sub.FileGenerateProcessor;
import com.btm.planb.diffobject.generate.processor.sub.JavaWriteProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 子处理器管理器
 */
public class SubProcessorManager {

    private static final List<AbstractSubProcessor> subProcessor = new ArrayList<>();

    public SubProcessorManager() {
        // 生成java文件的语句
        subProcessor.add(new FileGenerateProcessor());
        // 生成Java文件
        subProcessor.add(new JavaWriteProcessor());
    }

    public void process(ProcessContext processContext) {
        subProcessor.forEach(processor -> processor.process(processContext));
    }
}
