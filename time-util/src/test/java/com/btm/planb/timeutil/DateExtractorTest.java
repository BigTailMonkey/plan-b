package com.btm.planb.timeutil;


import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class DateExtractorTest {

    @Test
    public void doExtract() {
    }

    @Test
    public void hasDateString() {
    }

    @Test
    public void translate() {
        DateExtractor extractor = new DateExtractor();
        LocalDate expect;
        LocalDate actual;
        expect = LocalDate.of(2021,10,2);
        actual = extractor.translate("2021年10月2日");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021年10月02日");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021-10-02");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021-10-2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021/10/2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021.10.2");
        Assert.assertEquals(expect, actual);
        expect = LocalDate.of(2021,9,2);
        actual = extractor.translate("2021年9月2日");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021-09-02");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021-9-2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021/9/2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021/09/2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021/9/02");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("2021.9.2");
        Assert.assertEquals(expect, actual);
        expect = LocalDate.of(0,9,2);
        actual = extractor.translate("9月2日");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("9月2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("09-02");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("9-2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("9/2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("09/2");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("9/02");
        Assert.assertEquals(expect, actual);
        actual = extractor.translate("9.2");
        Assert.assertEquals(expect, actual);
    }
}