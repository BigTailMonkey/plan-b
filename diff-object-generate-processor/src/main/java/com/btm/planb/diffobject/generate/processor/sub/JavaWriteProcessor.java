package com.btm.planb.diffobject.generate.processor.sub;

import com.btm.planb.diffobject.generate.context.ProcessContext;
import com.btm.planb.diffobject.generate.info.ClassInfo;
import com.btm.planb.diffobject.generate.processor.AbstractSubProcessor;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class JavaWriteProcessor extends AbstractSubProcessor {

    @Override
    public void doProcess(ProcessContext processContext) {
        JavaFileObject jfo;
        Writer writer = null;
        try {
            for (ClassInfo classInfo : processContext.getClassInfos()) {
                if (classInfo.hasCompiled()) {
                    continue;
                }
                jfo = processContext.getFiler().createSourceFile(classInfo.classFileFullName(), new Element[]{});
                writer = jfo.openWriter();
                writer.write(classInfo.toString());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(writer)) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
