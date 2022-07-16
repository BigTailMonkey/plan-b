package com.btm.planb.demo.diffobject.generate.vo;

public class RequestModels {

    private Boolean userSex;
    private Integer userAge;
    private InnerModel innerModel;

    public InnerModel getInnerModel() {
        return innerModel;
    }

    public void setInnerModel(InnerModel innerModel) {
        this.innerModel = innerModel;
    }

    public Boolean getUserSex() {
        return userSex;
    }

    public void setUserSex(Boolean userSex) {
        this.userSex = userSex;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }
}
