package com.btm.planb.diffobject.generate.processor.sub;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.ClassInfo;
import com.btm.planb.diffobject.generate.info.MethodInfo;
import com.btm.planb.diffobject.generate.info.ParameterInfo;
import com.btm.planb.diffobject.generate.processor.AbstractSubProcessor;

public class ImportGenerateProcessor extends AbstractSubProcessor {

    @Override
    public void doProcess(ProcessContext processContext) {
        for(ClassInfo classInfo : processContext.getClassInfos()) {
            classInfo.addImport("java.util.Objects");
            for (MethodInfo methodInfo : classInfo.getMapMethodInfos()) {
                // 添加方法返回值类型的import信息
                classInfo.addImport(methodInfo.getReturnTypeElement().toString());
                // 添加方法参数类型的import信息
                for (ParameterInfo parameterInfo : methodInfo.getParameterInfos()) {
                    classInfo.addImport(parameterInfo.getClassFullName());
                }
            }
        }
    }
}
