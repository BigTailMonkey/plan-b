package com.btm.planb.timeutil;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DateDetectorTest {

    @Test
    public void testHasDateString() {
        List<String> strings;
        strings = DateDetector.hasDateString("这是一个新的需求 （6.24上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("6.24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （6-24上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("6-24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021-6-24上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021-6-24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021-06-24上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021-06-24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （6-24 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("6-24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021-6-24 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021-6-24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021-06-24 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021-06-24", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021-06-02 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021-06-02", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （6-2 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("6-2", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021-6-2 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021-6-2", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021年06月02日 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021年06月02日", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （6月2日 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("6月2日", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （6月24日 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("6月24日", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021年6月2日 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021年6月2日", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021年6月30日 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021年6月30日", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （2021年12月21日 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("2021年12月21日", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （21.11.04 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("21.11.04", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （21.11.4 上线）", false);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("21.11.4", strings.get(0));
        strings = DateDetector.hasDateString("这是一个新的需求 （6.12 上线）", true);
        Assert.assertEquals(1L, strings.size());
        Assert.assertEquals("12", strings.get(0));
    }

    @Test
    public void firstDateString() {
    }

    @Test
    public void lastDateString() {
    }
}