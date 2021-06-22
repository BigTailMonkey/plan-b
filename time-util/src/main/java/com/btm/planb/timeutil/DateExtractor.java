package com.btm.planb.timeutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期提取器，只能提取"年月日"日期信息，不支持提取"时分秒"时间信息
 *
 * @author btm
 * @time 20:24 2021/6/21
 */
public class DateExtractor {

    /**
     * 解析字符串中匹配到的第一个日期
     * @param strWithDate 包含"年月日"日期的字符串
     * @return 解析完成的'年月日'的日期
     */
    public LocalDate extractFirst(String strWithDate) {
        String dateString = firstDateString(strWithDate, false);
        if (Objects.nonNull(dateString)) {
            return doExtract(dateString, true);
        } else {
            return null;
        }
    }

    /**
     * 解析字符串中匹配到的最后一个日期
     * @param strWithDate 包含"年月日"日期的字符串
     * @return 解析完成的'年月日'的日期
     */
    public LocalDate extractLast(String strWithDate) {
        String dateString = lastDateString(strWithDate, false);
        if (Objects.nonNull(dateString)) {
            return doExtract(dateString, true);
        } else {
            return null;
        }
    }

    /**
     * 批量解析，默认不忽略小数点分割的日期，默认遇到异常不中断解析
     *
     * @param strWithDate 包含"年月日"日期的字符串
     * @return 解析完成的'年月日'的日期
     */
    public List<LocalDate> extract(String strWithDate) {
        return extract(strWithDate, false, true);
    }

    /**
     * 批量解析
     *
     * @param strWithDate 包含"年月日"日期的字符串
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"会被识别为日期"6月2日"；false：不忽略小数点号分割的日期
     * @param interruptWithThrow true：当发生异常时中断解析；false：当发生异常时就捕获异常，继续后续的解析
     * @return 解析完成的'年月日'的日期
     */
    public List<LocalDate> extract(String strWithDate, boolean ignoreDecimal, boolean interruptWithThrow) {
        List<String> dateStrings = hasDateString(strWithDate, ignoreDecimal);
        List<LocalDate> dateList = new ArrayList<>();
        for (String dateString : dateStrings) {
            LocalDate date = doExtract(dateString, interruptWithThrow);
            if (Objects.nonNull(date)) {
                dateList.add(date);
            }
        }
        return dateList;
    }

    /**
     * 执行解析
     *
     * @param dateString 仅包含"年月日"日期的字符串
     * @param interruptWithThrow true：当发生异常时中断解析；false：当发生异常时就捕获异常，继续后续的解析
     * @return 解析完成的'年月日'的日期
     */
    public LocalDate doExtract(String dateString, boolean interruptWithThrow) {
        if (!interruptWithThrow) {
            try {
                return translate(dateString);
            } catch (Throwable e) {
                return null;
            }
        } else {
            return translate(dateString);
        }
    }

    /**
     * 从字符串中识别并提取日期字符串，如果有则返回匹配到的日期
     *
     * @param strWithDate 包含"年月日"日期的字符串
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"会被识别为日期"6月2日"；false：不忽略小数点号分割的日期
     * @return 从字符串中识别到的所有字符串，若没有匹配到则返回长度为零的集合
     */
    public List<String> hasDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = new ArrayList<>();
        String dateFormatRegex = "[1-9]\\d{0,3}[-年/.]?(0?[1-9]|1\\d)[-月/.]?(0?[1-9]|[12]\\d)[日号]?";
        if (ignoreDecimal) {
            dateFormatRegex = "[1-9]\\d{0,3}[-年/]?(0?[1-9]|1\\d)[-月/]?(0?[1-9]|[12]\\d)[日号]?";
        }
        Pattern compile = Pattern.compile(dateFormatRegex);
        Matcher matcher = compile.matcher(strWithDate);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public String firstDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = hasDateString(strWithDate, ignoreDecimal);
        if (0 < result.size()) {
            return result.get(0);
        }
        return null;
    }

    public String lastDateString(String strWithDate, boolean ignoreDecimal) {
        List<String> result = hasDateString(strWithDate, ignoreDecimal);
        if (0 < result.size()) {
            return result.get(result.size() - 1);
        }
        return null;
    }

    /**
     * 对各种格式的事件进行
     * @param dateStrings 符合'年月日'格式的日期字符串
     * @return 解析后的日期
     */
    public LocalDate translate(String dateStrings) {
        if (Pattern.matches("^[1-9]\\d{0,3}-(0?[1-9]|1\\d)-(0?[1-9]|[12]\\d)$", dateStrings)) {
            // yyyy-mm-dd
            String[] dateStrArr = dateStrings.split("-");
            return LocalDate.of(Integer.parseInt(dateStrArr[0]),Integer.parseInt(dateStrArr[1]),Integer.parseInt(dateStrArr[2]));
        } else if (Pattern.matches("^(0?[1-9]|1\\d)-(0?[1-9]|[12]\\d)$", dateStrings)) {
            // mm-dd
            String[] dateStrArr = dateStrings.split("-");
            return LocalDate.of(0,Integer.parseInt(dateStrArr[0]),Integer.parseInt(dateStrArr[1]));
        } else if (Pattern.matches("^[1-9]\\d{0,3}/(0?[1-9]|1\\d)/(0?[1-9]|[12]\\d)$", dateStrings)) {
            // yyyy/mm/dd
            String[] dateStrArr = dateStrings.split("/");
            return LocalDate.of(Integer.parseInt(dateStrArr[0]),Integer.parseInt(dateStrArr[1]),Integer.parseInt(dateStrArr[2]));
        } else if (Pattern.matches("^(0?[1-9]|1\\d)/(0?[1-9]|[12]\\d)$", dateStrings)) {
            // mm/dd
            String[] dateStrArr = dateStrings.split("/");
            return LocalDate.of(0,Integer.parseInt(dateStrArr[0]),Integer.parseInt(dateStrArr[1]));
        } else if (Pattern.matches("^[1-9]\\d{0,3}.(0?[1-9]|1\\d).(0?[1-9]|[12]\\d)$", dateStrings)) {
            // yyyy.mm.dd
            String[] dateStrArr = dateStrings.split("\\.");
            return LocalDate.of(Integer.parseInt(dateStrArr[0]),Integer.parseInt(dateStrArr[1]),Integer.parseInt(dateStrArr[2]));
        } else if (Pattern.matches("^(0?[1-9]|1\\d).(0?[1-9]|[12]\\d)$", dateStrings)) {
            // mm.dd
            String[] dateStrArr = dateStrings.split("\\.");
            return LocalDate.of(0,Integer.parseInt(dateStrArr[0]),Integer.parseInt(dateStrArr[1]));
        } else if (Pattern.matches("^[1-9]\\d{0,3}年(0?[1-9]|1\\d)月(0?[1-9]|[12]\\d)[日号]$", dateStrings)) {
            // yyyy年mm月dd日；yyyy年mm月dd号
            int y = dateStrings.indexOf("年");
            int m = dateStrings.indexOf("月");
            int d = dateStrings.indexOf("日");
            if (d < 0) {
                d = dateStrings.indexOf("号");
            }
            int yyyy = Integer.parseInt(dateStrings.substring(0, y));
            int mm = Integer.parseInt(dateStrings.substring(y+1, m));
            int dd = Integer.parseInt(dateStrings.substring(m+1,d));
            return LocalDate.of(yyyy, mm, dd);
        } else if (Pattern.matches("^(0?[1-9]|1\\d)月(0?[1-9]|[12]\\d)[日号]$", dateStrings)) {
            // mm月dd日；mm月dd号
            int m = dateStrings.indexOf("月");
            int d = dateStrings.indexOf("日");
            if (d < 0) {
                d = dateStrings.indexOf("号");
            }
            int mm = Integer.parseInt(dateStrings.substring(0, m));
            int dd = Integer.parseInt(dateStrings.substring(m+1,d));
            return LocalDate.of(0, mm, dd);
        } else {
            throw new RuntimeException("日期解析失败,解析不支持的时间："+dateStrings);
        }
    }
}
