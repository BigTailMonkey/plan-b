package com.btm.planb.diffobject.generate.info;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodInfo {

    // 方法上注解信息
    private final Map<String, SpecifyKeyInfo> specifyKeyInfo = new HashMap<>();
    // 方法的参数信息
    private final List<ParameterInfo> parameterInfos = new ArrayList<>();
    // 方法的返回值信息
    private Element returnTypeElement;
    // 方法的名称
    private final String methodName;

    // 方法返回值对象的名称
    private static final String RETURN_VALUE_NAME = "$$result$$";
    // 方法型参的变量命名前缀
    private static final String PARAMETER_PREFIX = "$$parameter$$_";

    public MethodInfo(String methodName) {
        this.methodName = methodName;
    }

    public void addSpecifyKeyInfo(Map<String, SpecifyKeyInfo> infos) {
        this.specifyKeyInfo.putAll(infos);
    }

    public void setReturnTypeElement(Element returnTypeElement) {
        this.returnTypeElement = returnTypeElement;
    }

    public Map<String, SpecifyKeyInfo> getSpecifyKeyInfo() {
        return specifyKeyInfo;
    }

    public Element getReturnTypeElement() {
        return returnTypeElement;
    }

    public String getSourceName() {
        return PARAMETER_PREFIX + 0;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getReturnValueName() {
        return RETURN_VALUE_NAME;
    }

    public void addParameterInfo(List<ParameterInfo> parameterInfos) {
        for (ParameterInfo info : parameterInfos) {
            info.setVariableName(PARAMETER_PREFIX +this.parameterInfos.size() );
            this.parameterInfos.add(info);
        }
    }

    public List<ParameterInfo> getParameterInfos() {
        return this.parameterInfos;
    }

    public ParameterInfo getParameterInfo(int index) {
        if (index >= this.parameterInfos.size()) {
            return null;
        }
        return this.parameterInfos.get(index);
    }
}
