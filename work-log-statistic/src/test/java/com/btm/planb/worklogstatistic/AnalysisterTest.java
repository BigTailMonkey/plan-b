package com.btm.planb.worklogstatistic;

import org.junit.Assert;
import org.junit.Test;

public class AnalysisterTest {

    @Test
    public void statistic() {
    }

    @Test
    public void findFlanIndexOfLine() {
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx,"上线","发布","提测","设计");
        String flanIndexOfLine = analysister.findFlagStr("(5月12日)");
        Assert.assertNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("(5月12日上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("( 5月12日)");
        Assert.assertNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("( 5月12日上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("( 5月12日 )");
        Assert.assertNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("( 5月12日 上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("(  5月12日  )");
        Assert.assertNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("(  5月12日  发布)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("(将于5月12日上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(将于5月12日上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述（将于5月12日上线）");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述（将于5月12日上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(2021-01-01上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述( 2021-01-01上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(  2021-01-01  上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(2021-01-01 上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(2021年01月01日 上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(01-01 上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(1-01 上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述(1-1 上线)");
        Assert.assertNotNull(flanIndexOfLine);
        flanIndexOfLine = analysister.findFlagStr("需求内容描述( 1-1 上线)");
        Assert.assertNotNull(flanIndexOfLine);
    }

    @Test
    public void testStatistic() {
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx,"设计","提测","上线","发布");
        WorkLogStandLine standLine = analysister.statistic("TB-11221 测试项目 (2021-02-25 提测)", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime("2021-02-25");
        expectedLine.setWorkInfo("TB-11221测试项目");
        expectedLine.setSourceStr("TB-11221 测试项目 (2021-02-25 提测)");
        expectedLine.setFlag("提测");
        Assert.assertEquals(expectedLine, standLine);
    }

    @Test
    public void testStatistic2() {
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx,"设计","提测","上线","发布");
        WorkLogStandLine standLine = analysister.statistic("TB-11274  P0二类测试看看 （5月26号提测，6月1号上线）", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime("5月26号");
        expectedLine.setWorkInfo("TB-11274P0二类测试看看");
        expectedLine.setSourceStr("TB-11274  P0二类测试看看 （5月26号提测，6月1号上线）");
        expectedLine.setFlag("提测");
        Assert.assertEquals(expectedLine, standLine);
    }

    @Test
    public void testStatistic3() {
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx,"设计","提测","上线","发布");
        WorkLogStandLine standLine = analysister.statistic("TB-11274  P0有效期判断逻辑统一 （配合提测，6月1号上线）", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime("6月1号");
        expectedLine.setWorkInfo("TB-11274P0有效期判断逻辑统一");
        expectedLine.setSourceStr("TB-11274  P0有效期判断逻辑统一 （配合提测，6月1号上线）");
        expectedLine.setFlag("上线");
        Assert.assertEquals(expectedLine, standLine);
    }

    @Test
    public void testStatistic4() {
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx,"设计","提测","上线","发布");
        WorkLogStandLine standLine = analysister.statistic("1、TB-11186 【技术】交易服务慢SQL优化第八期（2021-05-18 上线）", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime("2021-05-18");
        expectedLine.setWorkInfo("TB-11186【技术】交易服务慢SQL优化第八期");
        expectedLine.setSourceStr("1、TB-11186 【技术】交易服务慢SQL优化第八期（2021-05-18 上线）");
        expectedLine.setFlag("上线");
        Assert.assertEquals(expectedLine, standLine);
    }
}