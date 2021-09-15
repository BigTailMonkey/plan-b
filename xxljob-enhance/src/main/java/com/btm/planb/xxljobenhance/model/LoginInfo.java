package com.btm.planb.xxljobenhance.model;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.List;

public class LoginInfo {

    private List<Connection.KeyVal> formDataList = new ArrayList<>();
    private String session;
    private String acwTc;

    public void addFormData(String paramKey, String paramValue) {
        formDataList.add(HttpConnection.KeyVal.create(paramKey,paramValue));
    }

    public void setUserName(String userName) {
        formDataList.add(HttpConnection.KeyVal.create("username",userName));
    }

    public void setPassword(String password) {
        formDataList.add(HttpConnection.KeyVal.create("password", password));
    }

    public List<Connection.KeyVal> getFormDataList() {
        return formDataList;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAcwTc() {
        return acwTc;
    }

    public void setAcwTc(String acwTc) {
        this.acwTc = acwTc;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "formDataList=" + formDataList +
                ", session='" + session + '\'' +
                ", acwTc='" + acwTc + '\'' +
                '}';
    }
}
