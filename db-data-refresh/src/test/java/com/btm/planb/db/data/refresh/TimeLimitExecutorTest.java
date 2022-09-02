package com.btm.planb.db.data.refresh;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TimeLimitExecutorTest {

    @Test
    public void active() {
        Set<Long> actual = new HashSet<>();
        Set<Long> expect = new HashSet<>();
        TestParameter testParameter = new TestParameter();
        testParameter.setName("junit test parameter");
        FlexibleExecutorFactory<TestParameter, DataInfo, Void> factory = new FlexibleExecutorFactory<>();
        TimeLimitExecutor<TestParameter, DataInfo, Void> executor = factory.name("junit test")
                .waitTime(10)
                .enableContinue((p, b) -> b.getMaxId() < 1)
                .dataSourceFunction((p, i) -> {
                    if (i.getMaxId() > 0) {
                        return Collections.emptyList();
                    }
                    List<DataInfo> list = new ArrayList<>(100);
                    for (int j = 0; j < 2; j++) {
                        actual.add((long) j);
                        list.add(new DataInfo(j, p.getName()));
                    }
                    return list;
                }).dataProcess(l -> {
                    try {
                        Thread.sleep(new Random().nextInt(9999));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DataInfo dataInfo = l.get(0);
                    expect.add(dataInfo.getId());
                    return null;
                }).build();
        executor.active(testParameter);
        assertEquals(actual, expect);
    }
}