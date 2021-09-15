package com.btm.planb.xxljobenhance.exceptions;

public class LoginException extends RuntimeException {

    public LoginException(String message) {
        super("登陆流程异常，" + message);
    }
}
