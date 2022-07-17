package com.btm.planb.diffobject.generate.processor.sub;

import com.btm.planb.diffobject.generate.code.CodeTemplate;
import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.*;
import com.btm.planb.diffobject.generate.processor.AbstractSubProcessor;
import com.btm.planb.diffobject.generate.util.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MethodGenerateProcessor extends AbstractSubProcessor {

    @Override
    public void doProcess(ProcessContext processContext) {
        List<ClassInfo> classInfos = processContext.getClassInfos();
        for (ClassInfo classInfo : classInfos) {
            if (classInfo.hasCompiled()) {
                continue;
            }
            List<MethodInfo> methodInfos = classInfo.getMapMethodInfos();
            for (MethodInfo methodInfo : methodInfos) {
                classInfo.addMethods(CodeTemplate.printMethod(methodInfo, methodBody(methodInfo)));
            }
        }
    }

    /**
     * 输出方法体内容
     * @param methodInfo 方法信息
     * @return 方法体
     */
    private String methodBody(MethodInfo methodInfo) {
        Map<String, ReturnFieldInfo> specifiesInfo = methodInfo.getReturnFieldInfos();
        Element returnTypeElement = methodInfo.getReturnTypeElement();
        List<? extends Element> innerElements = returnTypeElement.getEnclosedElements();
        String returnValueName = methodInfo.getReturnValueName();
        StringBuilder body = new StringBuilder();
        // 声明返回值对象
        body.append(CodeTemplate.printReturnObject(returnTypeElement.getSimpleName().toString(), returnValueName));
        for (Element innerElement : innerElements) {
            if (!ElementKind.FIELD.equals(innerElement.getKind())) {
                continue;
            }
            String fileName = innerElement.getSimpleName().toString();
            String initialUpperFileName = StringUtils.convertInitialUpper(fileName);
            ReturnFieldInfo returnFieldInfo = specifiesInfo.get(fileName);
            if (Objects.nonNull(returnFieldInfo)) {
                body.append(CodeTemplate.printIfNullCodeFragment(
                        methodInfo.getSourceName(),
                        returnValueName,
                        StringUtils.convertInitialUpper(fileName),
                        returnFieldInfo.getSourceParameterName(),
                        StringUtils.convertInitialUpper(returnFieldInfo.getParameterFileName())));
            } else {
                body.append(CodeTemplate.printSetterGetter(returnValueName, initialUpperFileName,
                        methodInfo.getSourceName(), StringUtils.convertInitialUpper(fileName)));
            }
        }
        body.append(CodeTemplate.printReturn(returnValueName));
        return body.toString();
    }

}
