package com.btm.planb.timeutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        String dateString = DateDetector.firstDateString(strWithDate, false);
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
        String dateString = DateDetector.lastDateString(strWithDate, false);
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
     * @param ignoreDecimal true：忽小数点分割的日期格式，即"6.12"不会被正确识别；false：不忽略小数点号分割的日期
     * @param interruptWithThrow true：当发生异常时中断解析；false：当发生异常时就捕获异常，继续后续的解析
     * @return 解析完成的'年月日'的日期
     */
    public List<LocalDate> extract(String strWithDate, boolean ignoreDecimal, boolean interruptWithThrow) {
        List<String> dateStrings = DateDetector.hasDateString(strWithDate, ignoreDecimal);
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
     * 对各种格式的时间进行格式化，将一下格式的时间解析为{@link LocalDate}对象<br/>
     * <ol>
     *     <li>yyyy-mm-dd</li>
     *     <li>mm-dd</li>
     *     <li>yyyy/mm/dd</li>
     *     <li>mm/dd</li>
     *     <li>yyyy.mm.dd</li>
     *     <li>mm.dd</li>
     *     <li>yyyy年mm月dd日</li>
     *     <li>mm月dd日</li>
     *     <li>yyyy年mm月dd号</li>
     *     <li>mm月dd号</li>
     *     <li>mm月dd</li>
     * </ol>
     * 若传入的日期字符串不符合以上格式则抛出异常
     *
     * @param dateStrings 符合格式的日期字符串
     * @return 解析后的日期
     * @throws DateFormatException 时间格式异常
     */
    public LocalDate translate(String dateStrings) throws DateFormatException{
        LocalDate result;
        EnumDateFormat dateFormat = EnumDateFormat.format(dateStrings);
        if (Objects.isNull(dateFormat)) {
            return null;
        }
        switch (dateFormat) {
            case YMD1:
            // yyyy-mm-dd
            String[] ymd1 = dateStrings.split("-");
            result = LocalDate.of(Integer.parseInt(ymd1[0]),Integer.parseInt(ymd1[1]),Integer.parseInt(ymd1[2]));
            break;
            case MD1:
            // mm-dd
            String[] md1 = dateStrings.split("-");
            result = LocalDate.of(0,Integer.parseInt(md1[0]),Integer.parseInt(md1[1]));
            break;
            case YMD2:
            // yyyy/mm/dd
            String[] ymd2 = dateStrings.split("/");
            result = LocalDate.of(Integer.parseInt(ymd2[0]),Integer.parseInt(ymd2[1]),Integer.parseInt(ymd2[2]));
            break;
            case MD2:
            // mm/dd
            String[] md2 = dateStrings.split("/");
            result = LocalDate.of(0,Integer.parseInt(md2[0]),Integer.parseInt(md2[1]));
            break;
            case YMD3:
            // yyyy.mm.dd
            String[] ymd3 = dateStrings.split("\\.");
            result = LocalDate.of(Integer.parseInt(ymd3[0]),Integer.parseInt(ymd3[1]),Integer.parseInt(ymd3[2]));
            break;
            case MD3:
            // mm.dd
            String[] md3 = dateStrings.split("\\.");
            result = LocalDate.of(0,Integer.parseInt(md3[0]),Integer.parseInt(md3[1]));
            break;
            case YMD4:
            case YMD5:
            // yyyy年mm月dd日；yyyy年mm月dd号
            int ymd_y = dateStrings.indexOf("年");
            int ymd_m = dateStrings.indexOf("月");
            int ymd_d = dateStrings.indexOf("日");
            if (ymd_d < 0) {
                ymd_d = dateStrings.indexOf("号");
            }
            int yyyy = Integer.parseInt(dateStrings.substring(0, ymd_y));
            int ymd_mm = Integer.parseInt(dateStrings.substring(ymd_y+1, ymd_m));
            int ymd_dd = Integer.parseInt(dateStrings.substring(ymd_m+1, ymd_d));
            result = LocalDate.of(yyyy, ymd_mm, ymd_dd);
            break;
            case MD4:
            case MD5:
            case MD6:
            // mm月dd日；mm月dd号；mm月dd
            int md_m = dateStrings.indexOf("月");
            int md_d = dateStrings.indexOf("日");
            if (md_d < 0) {
                md_d = dateStrings.indexOf("号");
            }
            if (md_d < 0) {
                md_d = dateStrings.length();
            }
            int md_mm = Integer.parseInt(dateStrings.substring(0, md_m));
            int md_dd = Integer.parseInt(dateStrings.substring(md_m+1,md_d));
            result = LocalDate.of(LocalDate.now().getYear(), md_mm, md_dd);
            if (result.isBefore(LocalDate.now())) {
                result.plusYears(1);
            }
            break;
            default:
            throw new DateFormatException("日期解析失败,解析不支持的时间："+dateStrings);
        }
        return result;
    }
}
