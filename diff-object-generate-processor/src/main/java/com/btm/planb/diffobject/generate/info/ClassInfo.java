package com.btm.planb.diffobject.generate.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 构建Java源码文件的关键信息
 */
public class ClassInfo {

    // 当前类继承的接口名称
    private final String interfaceName;
    // 当前类的方法信息
    private final List<MethodInfo> methodInfos = new ArrayList<>();
    // 当前类的import信息缓存
    private final Set<String> imports = new HashSet<>();
    // 当前类要生成的方法java语句缓存
    private final List<String> methods = new ArrayList<>();
    // 当前类是否已经被生成编译过
    private boolean compiled = false;

    public ClassInfo(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void addMapMethod(MethodInfo methodInfo) {
        this.methodInfos.add(methodInfo);
    }

    public List<MethodInfo> getMapMethodInfos() {
        return methodInfos;
    }

    public String getPackageName() {
        return interfaceName.substring(0, interfaceName.lastIndexOf("."));
    }

    public String getInterfaceName() {
        return interfaceName.substring(interfaceName.lastIndexOf(".") + 1);
    }

    public String printImport() {
        StringBuilder importsString = new StringBuilder();
        for (String imp : this.imports) {
            importsString.append("import ").append(imp).append(";\n");
        }
        importsString.append("\n");
        return importsString.toString();
    }

    public String printMethods() {
        StringBuilder methodsString = new StringBuilder();
        for (String imp : this.methods) {
            methodsString.append(imp).append("\n");
        }
        return methodsString.toString();
    }

    public void addImport(String imp) {
        this.imports.add(imp);
    }

    public void initImport() {
        initDefaultImport();
        for (MethodInfo methodInfo : methodInfos) {
            // 添加方法返回值类型的import信息
            this.addImport(methodInfo.getReturnTypeElement().toString());
            // 添加方法参数类型的import信息
            for (ParameterInfo parameterInfo : methodInfo.getParameterInfos()) {
                this.addImport(parameterInfo.getClassFullName());
            }
        }
    }

    public void addMethods(String method) {
        this.methods.add(method);
    }

    public String className() {
        return this.getInterfaceName() + "Impl";
    }

    public String classFileFullName() {
        return this.getPackageName() + "." + className();
    }

    public boolean hasCompiled() {
        return this.compiled;
    }

    @Override
    public String toString() {
        this.compiled = true;
        StringBuilder builder = new StringBuilder();
        builder.append("package "+getPackageName()+";\n\n");
        builder.append(this.printImport());
        builder.append("//Auto generated by diff-object-generate, do not modify!!\n\n");
        builder.append("public class "+className()+" implements "+getInterfaceName()+" { \n\n");
        builder.append(this.printMethods());
        builder.append("}");
        return builder.toString();
    }

    private void initDefaultImport() {
        this.imports.add("java.util.Objects");
    }
}
