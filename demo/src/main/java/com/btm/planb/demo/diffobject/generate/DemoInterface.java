package com.btm.planb.demo.diffobject.generate;

import com.btm.planb.demo.diffobject.generate.vo.DbDao;
import com.btm.planb.demo.diffobject.generate.vo.RequestModel;
import com.btm.planb.demo.diffobject.generate.vo.RequestModels;
import com.btm.planb.diffobject.generate.meta.Mappers;
import com.btm.planb.diffobject.generate.meta.Specify;

@Mappers
public interface DemoInterface {


    @Specify(source = "userSex", target = "sex")
    @Specify(source = "userAge", target = "age")
    DbDao change(DbDao dbDao, RequestModel model);

    @Specify(source = "userSex", target = "sex", clazz = RequestModel.class)
    @Specify(source = "userAge", target = "age", clazz = RequestModels.class)
    DbDao change2(DbDao dbDao, RequestModel model, RequestModels models);
}
