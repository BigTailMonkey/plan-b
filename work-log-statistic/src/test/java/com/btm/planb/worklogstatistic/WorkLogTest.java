package com.btm.planb.worklogstatistic;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkLogTest {

    @Test
    public void analysis() {
    }

    @Test
    public void distinguishRealWorkLog() {
        String oneLineLog;
        WorkLog workLog = new WorkLog();
        oneLineLog = "本周：";
        Assert.assertFalse(workLog.distinguishRealWorkLog(oneLineLog));
        oneLineLog = "下周：";
        Assert.assertFalse(workLog.distinguishRealWorkLog(oneLineLog));
        oneLineLog = "问题：";
        Assert.assertFalse(workLog.distinguishRealWorkLog(oneLineLog));
        oneLineLog = "上线：";
        Assert.assertFalse(workLog.distinguishRealWorkLog(oneLineLog));
        oneLineLog = "1、BTB-11186 【技术】交易服务慢SQL优化第八期（2021-05-18 上线）";
        Assert.assertTrue(workLog.distinguishRealWorkLog(oneLineLog));
    }
}