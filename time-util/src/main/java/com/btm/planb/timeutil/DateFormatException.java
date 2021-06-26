package com.btm.planb.timeutil;

/**
 * 时间格式异常，传入的字符串类型的时间，其格式不被支持
 */
public class DateFormatException extends RuntimeException {

    public DateFormatException(String message) {
        super(message);
    }
}
