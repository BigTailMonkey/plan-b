package com.btm.planb.diffobject.generate.util;

import com.btm.planb.diffobject.generate.meta.Specify;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

public final class AnnotationUtils {

    public static TypeMirror getClassTypeMirror(Specify specify) {
        try {
            specify.clazz();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        return null;
    }
}
