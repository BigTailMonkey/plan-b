package com.btm.planb.diffobject.generate.code;

import com.btm.planb.diffobject.generate.info.ClassInfo;
import com.btm.planb.diffobject.generate.info.MethodInfo;
import com.btm.planb.diffobject.generate.util.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CodeTemplate {

    private CodeTemplate() {}

    public static String printClass(ClassInfo classInfo) {
        String template = "package {0};\n\n" +
                "{1}\n" +
                "//Auto generated by diff-object-generate, do not modify!!\n\n" +
                "{2}";
        return MessageFormat.format(template, classInfo.getPackageName(), classInfo.printImport(), printClassDeclare(classInfo));
    }

    public static String printClassDeclare(ClassInfo classInfo) {
        String template = "public class {0} implements {1} '{'\n" +
                "{2}\n" +
                "'}'\n";
        return MessageFormat.format(template, classInfo.className(), classInfo.getInterfaceName(), classInfo.printMethods());
    }

    public static String printMethod(MethodInfo methodInfo, String body) {
        String template = "public {0} {1} ({2}) '{'\n" +
                "{3}" +
                "'}'\n";
        return MessageFormat.format(template, methodInfo.getReturnTypeElement().getSimpleName().toString(),
                methodInfo.getMethodName(), methodParameters(methodInfo), body);
    }

    public static String printIfNullCodeFragment(String templateObject, String targetName, String targetFileName, String sourceName, String sourceFileName) {
        String template = "if(Objects.nonNull({0}.get{1}())) '{'\n" +
                printSetterGetter(targetName, targetFileName, sourceName, sourceFileName) +
                "'}' else '{'\n" +
                printSetterGetter(targetName, targetFileName, templateObject, targetFileName) +
                "'}'\n";
        return MessageFormat.format(template, sourceName, StringUtils.convertInitialUpper(sourceFileName));
    }

    public static String printSetterGetter(String targetName, String targetFileName, String sourceName, String sourceFileName) {
        String template = "{0}.set{1}({2}.get{3}());\n";
        return MessageFormat.format(template, targetName, StringUtils.convertInitialUpper(targetFileName),
                sourceName, StringUtils.convertInitialUpper(sourceFileName));
    }

    public static String printReturnObject(String returnType, String returnObjectName) {
        String template = "{0} {1} = new {0}();\n";
        return MessageFormat.format(template, returnType, returnObjectName);
    }

    public static String printReturn(String returnObjectName) {
        String template = "return {0};\n";
        return MessageFormat.format(template, returnObjectName);
    }

    /**
     * 组装方法签名
     * @param methodInfo 方法信息
     * @return 方法签名
     */
    private static String methodParameters(MethodInfo methodInfo) {
        if (Objects.isNull(methodInfo.getParameterInfos()) || methodInfo.getParameterInfos().isEmpty()) {
            return "";
        }
        List<String> parameterStr = methodInfo.getParameterInfos().stream().map(item ->
                item.getName() + " " + item.getVariableName()).collect(Collectors.toList());
        return String.join(",", parameterStr);
    }

}