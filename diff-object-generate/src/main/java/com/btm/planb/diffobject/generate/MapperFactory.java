package com.btm.planb.diffobject.generate;

import java.lang.reflect.Constructor;

public class MapperFactory {

    public static <T> T get(Class<T> clazz) {
        T mapper = null;
        try {
            String canonicalName = clazz.getCanonicalName() + "Impl";
            Class<T> mapperClass = (Class<T>) Class.forName(canonicalName);
            Constructor<T> mapperClassConstructor = mapperClass.getConstructor();
            mapper = mapperClassConstructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapper;
    }

}
