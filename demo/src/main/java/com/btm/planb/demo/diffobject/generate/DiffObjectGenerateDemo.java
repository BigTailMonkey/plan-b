package com.btm.planb.demo.diffobject.generate;

import com.btm.planb.demo.diffobject.generate.vo.DbDao;
import com.btm.planb.demo.diffobject.generate.vo.RequestModel;

public class DiffObjectGenerateDemo {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("com.btm.planb.demo.diffobject.generate.DemoTargetImpl");
        DemoInterface o = (DemoInterface) aClass.newInstance();
        o.change(new DbDao(), new RequestModel());
    }
}
