package com.btm.planb.diffobject.generate.processor.sub;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.*;
import com.btm.planb.diffobject.generate.processor.AbstractSubProcessor;
import com.btm.planb.diffobject.generate.util.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
                StringBuilder methodStr = new StringBuilder("public").append(" ");
                methodStr.append(methodInfo.getReturnTypeElement().getSimpleName()).append(" ")
                        .append(methodSingle(methodInfo)).append(" {").append("\n")
                        .append(methodBody(methodInfo))
                        .append("}").append("\n");
                classInfo.addMethods(methodStr.toString());
            }
        }
    }

    /**
     * 组装方法签名
     * @param methodInfo 方法信息
     * @return 方法签名
     */
    private String methodSingle(MethodInfo methodInfo) {
        if (Objects.isNull(methodInfo.getParameterInfos()) || methodInfo.getParameterInfos().isEmpty()) {
            return "";
        }
        List<String> parameterStr = methodInfo.getParameterInfos().stream().map(item -> item.getName() + " " + item.getVariableName()).collect(Collectors.toList());
        return methodInfo.getMethodName() + "(" + String.join(",", parameterStr) + ")";
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
        body.append(returnTypeElement.getSimpleName()).append(" ")
                .append(returnValueName).append(" = new ")
                .append(returnTypeElement.getSimpleName()).append("();\n\n");
        for (Element innerElement : innerElements) {
            if (!ElementKind.FIELD.equals(innerElement.getKind())) {
                continue;
            }
            String fileName = innerElement.getSimpleName().toString();
            String initialUpperFileName = StringUtils.convertInitialUpper(fileName);
            ReturnFieldInfo returnFieldInfo = specifiesInfo.get(fileName);
            if (Objects.nonNull(returnFieldInfo)) {
                body.append("if (Objects.nonNull(")
                        .append(returnFieldInfo.getSourceParameterName())
                        .append(".get").append(StringUtils.convertInitialUpper(returnFieldInfo.getParameterFileName()))
                        .append("())) {\n");
                body.append(returnValueName)
                        .append(".set").append(initialUpperFileName).append("(")
                        .append(returnFieldInfo.printSourceParameterAndFileGetterMethod()).append(");\n");
                body.append("} else {\n");
                body.append(returnValueName)
                        .append(".set").append(initialUpperFileName).append("(")
                        .append(methodInfo.getSourceName()).append(".get")
                        .append(StringUtils.convertInitialUpper(fileName)).append("());\n");
                body.append("}\n");
            } else {
                body.append(returnValueName)
                        .append(".set").append(initialUpperFileName).append("(")
                        .append(methodInfo.getSourceName()).append(".get")
                        .append(StringUtils.convertInitialUpper(fileName)).append("());\n");
            }
        }
        body.append("return ").append(returnValueName).append(";\n");
        return body.toString();
    }

}
