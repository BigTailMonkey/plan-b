package com.btm.planb.timeutil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期检测器
 *
 * @author btm
 */
public class DateDetector {


    /**
     * 从字符串中识别并提取日期字符串，如果有则返回匹配到的日期
     *
     * @param strWithDate 包含"年月日"日期的字符串
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"会被识别为日期"6月2日"；false：不忽略小数点号分割的日期
     * @return 从字符串中识别到的所有字符串，若没有匹配到则返回长度为零的集合
     */
    public static List<String> hasDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = new ArrayList<>();
        String dateFormatRegex = "([1-9]\\d{0,3})?[-年/\\.]?(0?[1-9]|1\\d)[-月/\\.]?([12]\\d|0?[1-9])[日号]?";
        if (ignoreDecimal) {
            dateFormatRegex = "([1-9]\\d{0,3})?[-年/]?(0?[1-9]|1\\d)[-月/]?([12]\\d|0?[1-9])[日号]?";
        }
        Pattern compile = Pattern.compile(dateFormatRegex);
        Matcher matcher = compile.matcher(strWithDate);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public static String firstDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = hasDateString(strWithDate, ignoreDecimal);
        if (0 < result.size()) {
            return result.get(0);
        }
        return null;
    }

    public static String lastDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = hasDateString(strWithDate, ignoreDecimal);
        if (0 < result.size()) {
            return result.get(result.size() - 1);
        }
        return null;
    }
}
