package com.btm.planb.diffobject.generate.processor;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.ClassInfo;
import com.btm.planb.diffobject.generate.info.MethodInfo;
import com.btm.planb.diffobject.generate.info.ParameterInfo;
import com.btm.planb.diffobject.generate.info.SpecifyKeyInfo;
import com.btm.planb.diffobject.generate.meta.*;
import com.btm.planb.diffobject.generate.util.RoundEnvironmentUtil;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.btm.planb.diffobject.generate.MyAnnotation"})
public class MainProcessor extends AbstractProcessor {

    private ProcessContext processContext;
    private SubProcessorManager subProcessorManager;
    private RoundEnvironmentUtil roundEnvUtil;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processContext = new ProcessContext(processingEnv);
        subProcessorManager = new SubProcessorManager();
        this.roundEnvUtil = new RoundEnvironmentUtil(this.processContext.getTypeUtil());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> mappersElement = roundEnv.getElementsAnnotatedWith(Mappers.class);
        for (Element element : mappersElement) {
            if (!ElementKind.INTERFACE.equals(element.getKind())) {
                // 注解未使用在接口上，则直接忽略
                continue;
            }
            ClassInfo info = new ClassInfo(((TypeElement) element).getQualifiedName().toString());
            List<? extends Element> innerElements = element.getEnclosedElements();
            if (Objects.nonNull(innerElements) && !innerElements.isEmpty()) {
                for (Element innerElement : innerElements) {
                    if (ElementKind.METHOD.equals(innerElement.getKind())) {
                        MethodInfo methodInfo = generateMethod((ExecutableElement) innerElement, roundEnv.getRootElements());
                        info.addMapMethod(methodInfo);
                    }
                }
            }
            processContext.addGenerateClassKeyInfo(info);
        }
        subProcessorManager.process(processContext);
        return true;
    }

    /**
     * 针对接口中定义的一个方法，解析方法上的注解，并生成方法的语句
     * @param element 当前要解析的方法对象
     * @param allElements 当前正在编译的所有类信息
     * @return 要重写的类型转换方法信息
     */
    private MethodInfo generateMethod(ExecutableElement element, Set<? extends Element> allElements) {
        // 方法上的注解信息
        Map<String, Specify> specifies = prepareSpecifies(element.getAnnotationsByType(Specify.class));
        // 方法的参数列表
        List<? extends VariableElement> parameters = element.getParameters();
        TypeMirror returnType = element.getReturnType();
        // 记录与方法的返回值类型相同的方法入参的下标，后续取变更值时跳过此参数
        if (parameters.isEmpty() ||
                !this.processContext.getTypeUtil().isSameType(returnType, parameters.get(0).asType())) {
            this.processContext.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "方法" + element + "的第一个参数的类型与方法的返回值类型不一致");
        }
        MethodInfo methodInfo = new MethodInfo(element.getSimpleName().toString());
        // 与方法的返回值类型相同的方法入参
        methodInfo.setReturnTypeElement(this.roundEnvUtil.findCompilingElement(allElements,parameters.get(0)));
        // 解析方法的参数信息
        methodInfo.addParameterInfo(processMethodParameters(parameters, allElements));
        // 解析注解指定的字段的取值逻辑源
        methodInfo.addSpecifyKeyInfo(
                processSpecifyKeyInfo(parameters, specifies, allElements));
        return methodInfo;
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
     * @param parameters
     * @param specifies
     * @param allElements
     * @return
     */
    private Map<String/*target*/, SpecifyKeyInfo> processSpecifyKeyInfo(List<? extends VariableElement> parameters,
                                                                        Map<String, Specify> specifies,
                                                                        Set<? extends Element> allElements) {
        Map<String, SpecifyKeyInfo> specifyKeyInfos = new HashMap<>();
        for (int i = 1;i < parameters.size();i++) {
            Element element = this.roundEnvUtil.findCompilingElement(allElements, parameters.get(i));
            if (Objects.nonNull(element)) {
                List<? extends Element> innerElements = element.getEnclosedElements();
                for (Element filedElement : innerElements) {
                    if (!ElementKind.FIELD.equals(filedElement.getKind())) {
                        continue;
                    }
                    String filedName = filedElement.getSimpleName().toString();
                    if (specifies.containsKey(filedName)) {
                        Specify specify = specifies.get(filedName);
                        SpecifyKeyInfo specifyKeyInfo = new SpecifyKeyInfo((VariableElement)filedElement, element, specify, i);
                        specifyKeyInfos.put(specify.target(), specifyKeyInfo);
                    }
                }
            } else {
                // todo 没在当前编译中的包中，使用其他方式会的类信息
            }
        }
        return specifyKeyInfos;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Mappers.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
