package com.btm.planb.timeutil;

import java.util.regex.Pattern;

/**
 * 日期格式化，仅支持包含'年月日'的日期信息格式化，不能格式化'时分秒'时间信息
 *
 * @author btm
 * @time 20:21 2021/6/21
 */
public class DateFormat {

    /**
     * 将日期文本格式化为'M月D日'形式<br/>
     * 当前支持的入参格式如下：
     * <ol>
     *     <li>YYYY-MM-DD</li>
     *     <li>MM-DD</li>
     *     <li>M月D日</li>
     *     <li>M月D号</li>
     *     <li>MM.DD日</li>
     *     <li>MM.DD号</li>
     *     <li>MM.DD</li>
     * </ol>
     * 若以上格式都不能匹配则原样返回
     *
     * @param dateTime 日期字段
     * @return 格式化后的日期
     */
    public static String formatDateTime(String dateTime) {
        if (Pattern.matches("[1-9]{1,2}月[01]{0,1}[0-9]{1,2}[日号]", dateTime)) {
            return dateTime;
        }
        if (Pattern.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$", dateTime)) {
            String[] date = dateTime.split("-");
            return Integer.parseInt(date[1]) + "月" + Integer.parseInt(date[2]) + "日";
        }
        if (Pattern.matches("^\\d{1,2}-\\d{1,2}$", dateTime)) {
            String[] date = dateTime.split("-");
            return Integer.parseInt(date[0]) + "月" + Integer.parseInt(date[1]) + "日";
        }
        if (Pattern.matches("^[1-9]{1,2}\\.[01]{0,1}[0-9]{1,2}[日号]$", dateTime)) {
            return dateTime.replace(".", "月");
        }
        if (Pattern.matches("^[1-9]{1,2}\\.[01]{0,1}[0-9]{1,2}$", dateTime)) {
            return dateTime.replace(".", "月") + "日";
        }
        return dateTime;
    }
}
