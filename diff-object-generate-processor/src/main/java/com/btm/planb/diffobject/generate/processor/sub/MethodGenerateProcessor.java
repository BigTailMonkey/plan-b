package com.btm.planb.diffobject.generate.processor.sub;

import com.btm.planb.diffobject.generate.code.CodeTemplate;
import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.*;
import com.btm.planb.diffobject.generate.processor.AbstractSubProcessor;
import com.btm.planb.diffobject.generate.util.RoundEnvironmentUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * java类方法生成子处理器
 */
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
        RoundEnvironmentUtil roundEnvironmentUtil = new RoundEnvironmentUtil(this.typeUtil);
        Map<String, ReturnFieldInfo> specifiesInfo = methodInfo.getReturnFieldInfos();
        Element returnTypeElement = methodInfo.getReturnTypeElement();
        String returnObjectName = methodInfo.getReturnValueName();
        List<? extends Element> filedElements = roundEnvironmentUtil.findElementByKind(returnTypeElement.getEnclosedElements(), ElementKind.FIELD);
        StringBuilder body = new StringBuilder();
        // 声明返回值对象
        body.append(CodeTemplate.printNewObject(returnTypeElement.getSimpleName().toString(), returnObjectName));
        for (Element innerElement : filedElements) {
            String fileName = innerElement.getSimpleName().toString();
            ReturnFieldInfo returnFieldInfo = specifiesInfo.get(fileName);
            if (Objects.nonNull(returnFieldInfo)) {
                body.append(CodeTemplate.printIfNullCodeFragment(
                        methodInfo.getSourceName(),
                        returnObjectName,
                        fileName,
                        returnFieldInfo.getSourceParameterName(),
                        returnFieldInfo.getParameterFileName()));
            } else {
                body.append(CodeTemplate.printSetterGetter(returnObjectName, fileName,
                        methodInfo.getSourceName(), fileName));
            }
        }
        body.append(CodeTemplate.printReturn(returnObjectName));
        return body.toString();
    }

}
