package com.btm.planb.diffobject.generate.processor.sub;

import com.btm.planb.diffobject.generate.contact.Contact;
import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.*;
import com.btm.planb.diffobject.generate.meta.DefaultNull;
import com.btm.planb.diffobject.generate.meta.Mappers;
import com.btm.planb.diffobject.generate.meta.Specify;
import com.btm.planb.diffobject.generate.processor.AbstractSubProcessor;
import com.btm.planb.diffobject.generate.util.AnnotationUtils;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 注解解析子处理器
 */
public class AnnotationParseProcessor extends AbstractSubProcessor {

    @Override
    public void doProcess(ProcessContext pct) {
        Set<? extends Element> mappersElement = pct.getRoundEnvironment().getElementsAnnotatedWith(Mappers.class);
        for (Element element : mappersElement) {
            if (!ElementKind.INTERFACE.equals(element.getKind())) {
                // 注解未使用在接口上，则直接忽略
                continue;
            }
            ClassInfo info = new ClassInfo(((TypeElement) element).getQualifiedName().toString());
            List<? extends Element> innerElements = element.getEnclosedElements();
            if (Objects.nonNull(innerElements) && !innerElements.isEmpty()) {
                for (Element innerElement : innerElements) {
                    if (!ElementKind.METHOD.equals(innerElement.getKind())) {
                        continue;
                    }
                    ExecutableElement methodElement = (ExecutableElement) innerElement;
                    Set<? extends Element> allElements = this.roundEnv.getRootElements();
                    // 方法上的注解信息
                    Map<String, Specify> specifies = prepareSpecifies(methodElement.getAnnotationsByType(Specify.class));
                    // 方法的参数列表
                    List<? extends VariableElement> parameters = methodElement.getParameters();
                    // 方法的返回值类型
                    TypeMirror returnType = methodElement.getReturnType();
                    // 校验方法参数列表不能为空且第一个参数的类型必须与方法的返回值类型一致
                    if (parameters.isEmpty() || !this.typeUtil.isSameType(returnType, parameters.get(0).asType())) {
                        this.messager.printMessage(Diagnostic.Kind.ERROR, "方法" + methodElement + "的第一个参数的类型与方法的返回值类型不一致");
                    }

                    MethodInfo methodInfo = new MethodInfo(methodElement.getSimpleName().toString());
                    // 与方法的返回值元素信息
                    Element returnTypeElement = this.roundEnvUtil.findCompilingElement(allElements, parameters.get(0));
                    methodInfo.setReturnTypeElement(returnTypeElement);
                    // 解析方法的参数信息
                    methodInfo.addParameterInfo(processMethodParameters(parameters, allElements));
                    // 解析注解指定的字段的取值逻辑源
                    methodInfo.addSpecifyKeyInfo(
                            processReturnFieldInfo(parameters, specifies, allElements, returnTypeElement.getEnclosedElements()));
                    info.addMapMethod(methodInfo);
                }
            }
            pct.addGenerateClassKeyInfo(info);
        }
    }

    private List<ParameterInfo> processMethodParameters(List<? extends VariableElement> parElements,
                                                        Set<? extends Element> allElements) {
        if (Objects.isNull(parElements)) {
            return Collections.emptyList();
        }
        List<ParameterInfo> infos = new ArrayList<>();
        for (VariableElement varElement : parElements) {
            Element compilingElement = this.roundEnvUtil.findCompilingElement(allElements, varElement);
            ParameterInfo parameterInfo = new ParameterInfo(compilingElement);
            infos.add(parameterInfo);
        }
        return infos;
    }

    /**
     * 预处理specify注解信息
     * @param specifies specify注解
     * @return specify注解信息
     */
    private Map<String/*source*/, Specify> prepareSpecifies(Specify[] specifies) {
        if (Objects.nonNull(specifies) && specifies.length > 0) {
            return Arrays.stream(specifies).collect(Collectors.toMap(Specify::source, s -> s, (o, n) -> o));
        } else {
            return new HashMap<>();
        }
    }

    /**
     * 解析Specify注解信息
     * @param parameters 方法的参数
     * @param specifies 方法上的Specify注解
     * @param allElements 当前轮次环境的全部元素数据
     * @param returnTypeElement 方法的返回值对象信息
     * @return 方法的返回值对象信息，key-方法返回值对象的字段名称，value-解析到的对应信息数据
     */
    private Map<String, ReturnFieldInfo> processReturnFieldInfo(List<? extends VariableElement> parameters,
                                                                          Map<String, Specify> specifies,
                                                                          Set<? extends Element> allElements,
                                                                          List<? extends Element> returnTypeElement) {
        Map<String, ReturnFieldInfo> returnFieldInfos = new HashMap<>();
        for (int i = 1;i < parameters.size();i++) {
            // 从本轮编译环境中找到依赖对象的class信息元素
            Element element = this.roundEnvUtil.findCompilingElement(allElements, parameters.get(i));
            if (Objects.nonNull(element)) {
                // 找到依赖对象的内部元素
                List<? extends Element> innerElements = this.roundEnvUtil.findElementByKind(element.getEnclosedElements(), ElementKind.FIELD);
                for (Element filedElement : innerElements) {
                    String filedName = filedElement.getSimpleName().toString();
                    if (returnFieldInfos.containsKey(filedName)) {
                        this.messager.printMessage(Diagnostic.Kind.ERROR,"重复的字段名称：" + filedName);
                    }
                    if (specifies.containsKey(filedName)) {
                        Specify specify = specifies.get(filedName);
                        TypeMirror classTypeMirror = AnnotationUtils.getClassTypeMirror(specify);
                        if (Objects.nonNull(classTypeMirror) && !DefaultNull.class.getCanonicalName().equals(classTypeMirror.toString())
                                && !element.asType().toString().equals(classTypeMirror.toString())) {
                            // 若注解指定了取值的类信息且类信息与当前处理元素不一致，则略过
                            continue;
                        }
                        ReturnFieldInfo returnFieldInfo = new ReturnFieldInfo(filedName,
                                Contact.PARAMETER_PREFIX + i, specify.source(), specify, (VariableElement)filedElement, element);
                        returnFieldInfos.put(specify.target(), returnFieldInfo);
                    } else {
                        for (Element returnElement : this.roundEnvUtil.findElementByKind(returnTypeElement, ElementKind.FIELD)) {
                            if (filedName.equals(returnElement.getSimpleName().toString())) {
                                returnFieldInfos.put(filedName, new ReturnFieldInfo(filedName,
                                        Contact.PARAMETER_PREFIX + i, (VariableElement)filedElement, element));
                            }
                        }
                    }
                }
            } else {
                // todo 没在当前编译中的包中，使用其他包中的类信息
                this.messager.printMessage(Diagnostic.Kind.ERROR, "暂不支持依赖包中的对象：" + parameters.get(i).asType());
            }
        }
        return returnFieldInfos;
    }
}
