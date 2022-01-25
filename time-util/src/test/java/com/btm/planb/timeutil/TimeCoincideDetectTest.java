package com.btm.planb.timeutil;

import com.btm.planb.timeutil.model.IPeriod;
import com.btm.planb.timeutil.model.PeriodDetectInfoModel;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeCoincideDetectTest extends TestCase {

    /**
     * 一切正常
     */
    @Test
    public void testDetect1() {
        TimeCoincideDetect timeCoincideDetect = new TimeCoincideDetect();
        List<IPeriod> periods = new ArrayList<>();
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:00:00"), transform("2022-01-01 02:00:00")));
        String detect = timeCoincideDetect.detect(periods, 0);
        Assert.assertNull(detect);
    }

    /**
     * 存在两次完全的重复
     */
    @Test
    public void testDetect2() {
        TimeCoincideDetect timeCoincideDetect = new TimeCoincideDetect();
        List<IPeriod> periods = new ArrayList<>();
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:00:00"), transform("2022-01-01 02:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        String detect = timeCoincideDetect.detect(periods, 2);
        Assert.assertNull(detect);
    }

    /**
     * 存在三次重复
     */
    @Test
    public void testDetect3() {
        TimeCoincideDetect timeCoincideDetect = new TimeCoincideDetect();
        List<IPeriod> periods = new ArrayList<>();
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:00:00"), transform("2022-01-01 02:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 05:00:00"), transform("2022-01-01 06:00:00")));
        String detect = timeCoincideDetect.detect(periods, 3);
        Assert.assertNull(detect);
    }

    /**
     * 存在四次重复
     */
    @Test
    public void testDetect4() {
        TimeCoincideDetect timeCoincideDetect = new TimeCoincideDetect();
        List<IPeriod> periods = new ArrayList<>();
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:00:00"), transform("2022-01-01 02:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 05:00:00"), transform("2022-01-01 06:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 05:30:00")));
        String detect = timeCoincideDetect.detect(periods, 4);
        Assert.assertNull(detect);
    }

    /**
     * 存在五次重复
     */
    @Test
    public void testDetect5() {
        TimeCoincideDetect timeCoincideDetect = new TimeCoincideDetect();
        List<IPeriod> periods = new ArrayList<>();
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:00:00"), transform("2022-01-01 02:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 05:00:00"), transform("2022-01-01 06:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 05:30:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:55:00"), transform("2022-01-01 02:30:00")));
        String detect = timeCoincideDetect.detect(periods, 4);
        Assert.assertNull(detect);
    }

    /**
     * 存在五次重复
     */
    @Test
    public void testDetect6() {
        TimeCoincideDetect timeCoincideDetect = new TimeCoincideDetect();
        List<IPeriod> periods = new ArrayList<>();
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 01:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 00:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 01:00:00"), transform("2022-01-01 02:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 03:00:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 02:00:00"), transform("2022-01-01 05:30:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 05:00:00"), transform("2022-01-01 06:40:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 05:55:00"), transform("2022-01-01 06:50:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 05:56:00"), transform("2022-01-01 07:30:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 06:10:00"), transform("2022-01-01 06:30:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 06:30:00"), transform("2022-01-01 07:40:00")));
        periods.add(new PeriodDetectInfoModel(transform("2022-01-01 07:30:00"), transform("2022-01-01 07:50:00")));
        String detect = timeCoincideDetect.detect(periods, 5);
        Assert.assertNull(detect);
    }

    private LocalDateTime transform(String timeStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timeStr, df);
    }
}