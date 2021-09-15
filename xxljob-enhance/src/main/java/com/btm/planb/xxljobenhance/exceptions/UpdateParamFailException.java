package com.btm.planb.xxljobenhance.exceptions;

public class UpdateParamFailException extends JobException {

    public UpdateParamFailException(String message) {
        super("更新参数发生异常，" + message);
    }
}
