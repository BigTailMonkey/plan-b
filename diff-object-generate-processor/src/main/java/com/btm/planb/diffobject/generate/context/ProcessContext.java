package com.btm.planb.diffobject.generate.context;

import com.btm.planb.diffobject.generate.info.ClassInfo;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

public class ProcessContext {


    private Messager messager;
    private Filer filer;
    private Elements elementUtil;
    private Types typeUtil;
    private List<ClassInfo> classInfos = new ArrayList<>();

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
}
