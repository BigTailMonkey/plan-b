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
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"不会被正切识别；false：不忽略小数点号分割的日期
     * @return 从字符串中识别到的所有字符串，若没有匹配到则返回长度为零的集合
     */
    public static List<String> hasDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = new ArrayList<>();
        String dateFormatRegex = Constant.DATE_FORMAT;
        if (ignoreDecimal) {
            dateFormatRegex = Constant.DATE_FORMAT_IGNORE_DECIMAL;
        }
        Pattern compile = Pattern.compile(dateFormatRegex);
        Matcher matcher = compile.matcher(strWithDate);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 当从被解析的字符串中能解析出多个日期时，只返回第一个日期
     * @param strWithDate 被解析的字符串
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"不会被正切识别；false：不忽略小数点号分割的日期
     * @return 日期
     */
    public static String firstDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = hasDateString(strWithDate, ignoreDecimal);
        if (0 < result.size()) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 当从被解析的字符串中能解析出多个日期时，只返回最后一个日期
     * @param strWithDate 被解析的字符串
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"不会被正切识别；false：不忽略小数点号分割的日期
     * @return 日期
     */
    public static String lastDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = hasDateString(strWithDate, ignoreDecimal);
        if (0 < result.size()) {
            return result.get(result.size() - 1);
        }
        return null;
    }
}
