package com.btm.planb.timeutil;

import java.util.regex.Pattern;

public enum EnumDateFormat {

    YMD1("yyyy-mm-dd", "^[1-9]\\d{0,3}-(0?[1-9]|1\\d)-([12]\\d|0?[1-9])$"),
    YMD2("yyyy/mm/dd", "^[1-9]\\d{0,3}/(0?[1-9]|1\\d)/([12]\\d|0?[1-9])$"),
    YMD3("yyyy.mm.dd", "^[1-9]\\d{0,3}\\.(0?[1-9]|1\\d)\\.([12]\\d|0?[1-9])$"),
    YMD4("yyyy年mm月dd日", "^[1-9]\\d{0,3}年(0?[1-9]|1\\d)月([12]\\d|0?[1-9])日$"),
    YMD5("yyyy年mm月dd号", "^[1-9]\\d{0,3}年(0?[1-9]|1\\d)月([12]\\d|0?[1-9])号$"),

    MD1("mm-dd", "^(0?[1-9]|1\\d)-([12]\\d|0?[1-9])$"),
    MD2("mm/dd", "^(0?[1-9]|1\\d)/([12]\\d|0?[1-9])$"),
    MD3("mm.dd", "^(0?[1-9]|1\\d)\\.([12]\\d|0?[1-9])$"),
    MD4("mm月dd日", "^(0?[1-9]|1\\d)月([12]\\d|0?[1-9])日$"),
    MD5("mm月dd号", "^(0?[1-9]|1\\d)月([12]\\d|0?[1-9])号$"),
    MD6("mm月dd", "^(0?[1-9]|1\\d)月([12]\\d|0?[1-9])$"),
    ;

    /**
     * 日期格式
     */
    private final String format;
    /**
     * 日期格式对应的正则表达式
     */
    private final String regex;

    EnumDateFormat(String format, String regex) {
        this.format = format;
        this.regex = regex;
    }

    public static EnumDateFormat format(String dateStrings) {
        for (EnumDateFormat dateFormat : EnumDateFormat.values()) {
            if (Pattern.matches(dateFormat.regex, dateStrings)) {
                return dateFormat;
            }
        }
        throw new DateFormatException("日期解析失败,解析不支持的时间："+dateStrings);
    }

    public String getFormat() {
        return format;
    }
}
