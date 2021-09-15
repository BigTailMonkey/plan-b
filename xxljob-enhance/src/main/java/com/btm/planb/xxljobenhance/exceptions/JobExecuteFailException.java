package com.btm.planb.xxljobenhance.exceptions;

public class JobExecuteFailException extends PostRequestFailException {

    public JobExecuteFailException(String message) {
        super("xxl-job定时任务执行失败，" + message);
    }
}
