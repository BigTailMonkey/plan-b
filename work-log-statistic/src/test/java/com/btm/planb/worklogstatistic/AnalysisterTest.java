package com.btm.planb.worklogstatistic;

import com.btm.planb.worklogstatistic.keyNode.INode;
import com.btm.planb.worklogstatistic.keyNode.NodeChain;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class AnalysisterTest {

    @Test
    public void statistic() {
    }

    @Test
    public void testStatistic() {
        NodeChain nodeChain = new NodeChain();
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx);
        WorkLogStandLine standLine = analysister.statistic("", "TB-11221 测试项目 (2021-02-25 提测)", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime(LocalDate.of(2021,5,25));
        expectedLine.setWorkInfo("TB-11221测试项目");
        expectedLine.setSourceStr("TB-11221 测试项目 (2021-02-25 提测)");
        expectedLine.setKeyNode(nodeChain.findNode("提测", LocalDate.now()));
        Assert.assertEquals(expectedLine, standLine);
    }

    @Test
    public void testStatistic2() {
        NodeChain nodeChain = new NodeChain();
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx);
        WorkLogStandLine standLine = analysister.statistic("", "TB-11274  P0二类测试看看 （5月26号提测，6月1号上线）", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime(LocalDate.of(0,5,26));
        expectedLine.setWorkInfo("TB-11274P0二类测试看看");
        expectedLine.setSourceStr("TB-11274  P0二类测试看看 （5月26号提测，6月1号上线）");
        expectedLine.setKeyNode(nodeChain.findNode("提测", LocalDate.now()));
        Assert.assertEquals(expectedLine, standLine);
    }

    @Test
    public void testStatistic3() {
        NodeChain nodeChain = new NodeChain();
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx);
        WorkLogStandLine standLine = analysister.statistic("", "TB-11274  P0有效期判断逻辑统一 （配合提测，6月1号上线）", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime(LocalDate.of(0,6,1));
        expectedLine.setWorkInfo("TB-11274P0有效期判断逻辑统一");
        expectedLine.setSourceStr("TB-11274  P0有效期判断逻辑统一 （配合提测，6月1号上线）");
        expectedLine.setKeyNode(nodeChain.findNode("上线", LocalDate.now()));
        Assert.assertEquals(expectedLine, standLine);
    }

    @Test
    public void testStatistic4() {
        NodeChain nodeChain = new NodeChain();
        String demandNumberRegex = "^[tT][Bb]-\\d{1,5}";
        String regx = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        Analysister analysister = new Analysister(regx);
        WorkLogStandLine standLine = analysister.statistic("", "1、TB-11186 【技术】交易服务慢SQL优化第八期（2021-05-18 上线）", demandNumberRegex);
        WorkLogStandLine expectedLine = new WorkLogStandLine(demandNumberRegex);
        expectedLine.setDatetime(LocalDate.of(0,5,18));
        expectedLine.setWorkInfo("TB-11186【技术】交易服务慢SQL优化第八期");
        expectedLine.setSourceStr("1、TB-11186 【技术】交易服务慢SQL优化第八期（2021-05-18 上线）");
        expectedLine.setKeyNode(nodeChain.findNode("上线", LocalDate.now()));
        Assert.assertEquals(expectedLine, standLine);
    }
}