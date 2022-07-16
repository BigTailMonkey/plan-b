package com.btm.planb.demo.diffobject.generate;

import com.btm.planb.demo.diffobject.generate.vo.DbDao;
import com.btm.planb.demo.diffobject.generate.vo.InnerModel;
import com.btm.planb.demo.diffobject.generate.vo.RequestModel;
import com.btm.planb.demo.diffobject.generate.vo.RequestModels;
import com.btm.planb.diffobject.generate.MapperFactory;

import java.util.Objects;

public class DiffObjectGenerateDemo {

    public static void main(String[] args) throws IllegalAccessException {
        DemoInterface o = MapperFactory.get(DemoInterface.class);
        DbDao source = new DbDao();
        source.setAge(20);
        source.setName("source");
        source.setSex(true);
        RequestModel model = new RequestModel();
        model.setName("modify");
        model.setUserAge(25);
        model.setUserSex(false);
        DbDao target = o.change(source, model);
        if (Objects.isNull(target)) {
            throw new NullPointerException("对象生成错误");
        }
        if (!target.getName().equals(model.getName())) {
            throw new IllegalAccessException("name 字段值错误");
        }
        if (!target.getSex().equals(model.getUserSex())) {
            throw new IllegalAccessException("sex 字段值错误");
        }
        if (!target.getAge().equals(model.getUserAge())) {
            throw new IllegalAccessException("age 字段值错误");
        }

        RequestModels models = new RequestModels();
        models.setUserAge(30);
        models.setUserSex(true);
        models.setInnerModel(new InnerModel());
        DbDao targets = o.change2(source, model, models);
        if (Objects.isNull(targets)) {
            throw new NullPointerException("对象生成错误");
        }
        if (!targets.getName().equals(model.getName())) {
            throw new IllegalAccessException("name 字段值错误");
        }
        if (!targets.getSex().equals(model.getUserSex())) {
            throw new IllegalAccessException("sex 字段值错误");
        }
        if (!targets.getAge().equals(models.getUserAge())) {
            throw new IllegalAccessException("age 字段值错误");
        }
        if (targets.getInnerModel() != models.getInnerModel()) {
            throw new IllegalAccessException("inner 字段值错误");
        }
    }
}
