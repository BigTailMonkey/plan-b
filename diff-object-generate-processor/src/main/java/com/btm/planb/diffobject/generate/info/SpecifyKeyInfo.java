package com.btm.planb.diffobject.generate.info;


import com.btm.planb.diffobject.generate.meta.Specify;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

public class SpecifyKeyInfo {

    // 注解信息
    private final Specify specify;
    // 注解的source字段所指明的字段元素信息
    private final VariableElement filedElement;
    // 注解的source字段所指明的字段元素所在的类元素信息
    private final Element filedParentElement;
    // 当前注解的source字段所属的对象在参数列表中的下标位置
    private final int parameterListIndex;

    public SpecifyKeyInfo(VariableElement element, Element filedParentElement, Specify specify, int parameterListIndex) {
        this.filedElement = element;
        this.filedParentElement = filedParentElement;
        this.specify = specify;
        this.parameterListIndex = parameterListIndex;
    }

    public Specify getSpecify() {
        return specify;
    }

    public VariableElement getFiledElement() {
        return filedElement;
    }

    public Element getFiledParentElement() {
        return filedParentElement;
    }

    public int getParameterListIndex() {
        return parameterListIndex;
    }
}
