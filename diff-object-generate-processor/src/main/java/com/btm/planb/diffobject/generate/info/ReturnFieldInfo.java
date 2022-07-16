package com.btm.planb.diffobject.generate.info;

import com.btm.planb.diffobject.generate.meta.Specify;
import com.btm.planb.diffobject.generate.util.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * 方法的返回值对象字段信息
 */
public class ReturnFieldInfo {

    // 返回值对象的字段名称
    private final String returnObjectFieldName;
    // 数据来源的参数名称
    private final String sourceParameterName;
    // 数据来源的参数的字段名称
    private final String parameterFileName;
    // 是否通过注解指定了数据取值逻辑
    private final boolean specify;
    // 注解信息
    private final Specify specifyInfo;
    // 注解的source字段所指明的字段元素信息
    private final VariableElement filedElement;
    // 注解的source字段所指明的字段元素所在的类元素信息
    private final Element filedParentElement;

    public ReturnFieldInfo(String returnObjectFieldName, String sourceParameterName, VariableElement filedElement, Element filedParentElement) {
        this.returnObjectFieldName = returnObjectFieldName;
        this.sourceParameterName = sourceParameterName;
        this.parameterFileName = null;
        this.specify = false;
        this.specifyInfo = null;
        this.filedElement = filedElement;
        this.filedParentElement = filedParentElement;
    }

    public ReturnFieldInfo(String returnObjectFieldName, String sourceParameterName, String parameterFileName, Specify specifyInfo, VariableElement filedElement, Element filedParentElement) {
        this.returnObjectFieldName = returnObjectFieldName;
        this.sourceParameterName = sourceParameterName;
        this.parameterFileName = parameterFileName;
        this.specify = true;
        this.specifyInfo = specifyInfo;
        this.filedElement = filedElement;
        this.filedParentElement = filedParentElement;
    }

    public String getReturnObjectFieldName() {
        return returnObjectFieldName;
    }

    public String getSourceParameterName() {
        return sourceParameterName;
    }

    public String getParameterFileName() {
        return isSpecify() ? parameterFileName :returnObjectFieldName;
    }

    public boolean isSpecify() {
        return specify;
    }

    public Specify getSpecifyInfo() {
        return specifyInfo;
    }

    public VariableElement getFiledElement() {
        return filedElement;
    }

    public Element getFiledParentElement() {
        return filedParentElement;
    }

    public String printSourceParameterAndFileGetterMethod() {
        return this.sourceParameterName + ".get" +
                StringUtils.convertInitialUpper(getParameterFileName()) +
                "()";
    }
}
