package com.btm.planb.timeutil;

import java.time.LocalDate;
import java.util.Objects;

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
        DateExtractor extractor = new DateExtractor();
        LocalDate localDate = extractor.extractFirst(dateTime);
        if (Objects.isNull(localDate)) {
            return dateTime;
        } else {
            return localDate.getMonthValue() + "月" + localDate.getDayOfMonth() + "日";
        }
    }
}
