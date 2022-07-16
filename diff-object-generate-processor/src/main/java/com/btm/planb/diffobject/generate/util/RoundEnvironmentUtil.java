package com.btm.planb.diffobject.generate.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class RoundEnvironmentUtil {

    private final Types typeUtil;

    public RoundEnvironmentUtil(Types typeUtil) {
        this.typeUtil = typeUtil;
    }


    /**
     * 根据元素信息，获取对应的正在编译的class信息
     * @param elements 备选编译元素集合
     * @param target 目标元素信息
     * @return 找到的正在编译的class信息
     */
    public Element findCompilingElement(Set<? extends Element> elements, Element target) {
        Element element = null;
        for (Element rootElement : elements) {
            if (typeUtil.isAssignable(rootElement.asType(), target.asType())) {
                element = rootElement;
            }
        }
        return element;
    }

    public List<? extends Element> findElementByKind(List<? extends Element> elements, ElementKind kind) {
        if (Objects.isNull(elements) || elements.isEmpty()) {
            return Collections.emptyList();
        } else {
            return elements.stream().filter(item -> kind.equals(item.getKind())).collect(Collectors.toList());
        }
    }

}
