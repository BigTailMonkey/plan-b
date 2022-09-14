package com.btm.planb.diffobject.generate.context;

import com.btm.planb.diffobject.generate.info.ClassInfo;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理流程上下文，
 * 用于在各个处理器、子处理器之间传递数据
 */
public class ProcessContext {

    private final Messager messager;
    private final Filer filer;
    private final Elements elementUtil;
    private final Types typeUtil;
    private final List<ClassInfo> classInfos = new ArrayList<>();
    // 当前此轮编译的环境
    private RoundEnvironment roundEnvironment;

    public ProcessContext(ProcessingEnvironment processingEnv) {
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elementUtil = processingEnv.getElementUtils();
        typeUtil = processingEnv.getTypeUtils();
    }

    public void addGenerateClassKeyInfo(ClassInfo info) {
        this.classInfos.add(info);
    }

    public Messager getMessager() {
        return messager;
    }

    public Filer getFiler() {
        return filer;
    }

    public Elements getElementUtil() {
        return elementUtil;
    }

    public Types getTypeUtil() {
        return typeUtil;
    }

    public List<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public RoundEnvironment getRoundEnvironment() {
        return roundEnvironment;
    }

    public void setRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }
}
