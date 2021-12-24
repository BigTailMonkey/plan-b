package com.btm.planb.timeutil;

import java.util.regex.Pattern;

/**
 * 时间格式枚举
 */
public enum EnumDateFormat {

    YMD1("(yy)yy-mm-dd", "^"+Constant.YEAR_FORMAT+"-"+Constant.MONTH_FORMAT+"-"+Constant.DAY_FORMAT+"$"),
    YMD2("(yy)yy/mm/dd", "^"+Constant.YEAR_FORMAT+"/"+Constant.MONTH_FORMAT+"/"+Constant.DAY_FORMAT+"$"),
    YMD3("(yy)yy.mm.dd", "^"+Constant.YEAR_FORMAT+"\\."+Constant.MONTH_FORMAT+"\\."+Constant.DAY_FORMAT+"$"),
    YMD4("(yy)yy年mm月dd日", "^"+Constant.YEAR_FORMAT+"年"+Constant.MONTH_FORMAT+"月"+Constant.DAY_FORMAT+"日$"),
    YMD5("(yy)yy年mm月dd号", "^"+Constant.YEAR_FORMAT+"年"+Constant.MONTH_FORMAT+"月"+Constant.DAY_FORMAT+"号$"),

    MD1("mm-dd", "^"+Constant.MONTH_FORMAT+"-"+Constant.DAY_FORMAT+"$"),
    MD2("mm/dd", "^"+Constant.MONTH_FORMAT+"/"+Constant.DAY_FORMAT+"$"),
    MD3("mm.dd", "^"+Constant.MONTH_FORMAT+"\\."+Constant.DAY_FORMAT+"$"),
    MD4("mm月dd日", "^"+Constant.MONTH_FORMAT+"月"+Constant.DAY_FORMAT+"日$"),
    MD5("mm月dd号", "^"+Constant.MONTH_FORMAT+"月"+Constant.DAY_FORMAT+"号$"),
    MD6("mm月dd", "^"+Constant.MONTH_FORMAT+"月"+Constant.DAY_FORMAT+"$"),
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
        return null;
    }

    public String getFormat() {
        return format;
    }
}
