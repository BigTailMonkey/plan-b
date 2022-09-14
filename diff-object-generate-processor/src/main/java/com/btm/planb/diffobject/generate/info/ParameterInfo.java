package com.btm.planb.diffobject.generate.info;

import javax.lang.model.element.Element;

/**
 * 构建Java源码文件的关键信息，参数信息
 */
public class ParameterInfo {

    // 参数类的全路径名
    private final String classFullName;
    // 参数类的名称
    private final String name;
    // 参数声明的变量名称
    private String variableName;
    // 参数类型的解析信息
    private final Element element;

    public ParameterInfo(Element element) {
        this.element = element;
        this.classFullName = element.toString();
        this.name = element.getSimpleName().toString();
    }
    public String getClassFullName() {
        return classFullName;
    }

    public String getName() {
        return name;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Element getElement() {
        return element;
    }
}
