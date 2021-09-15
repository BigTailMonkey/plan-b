package com.btm.planb.xxljobenhance.exceptions;

public class UserInfoException extends RuntimeException {

    public UserInfoException() {
        super("请检查配置文件中的用户信息是否完整");
    }
}
