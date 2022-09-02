package com.btm.planb.db.data.refresh;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CountNumberExecutorTest {

    @Test
    public void active() {
        Set<Long> actual = new HashSet<>();
        Set<Long> expect = new HashSet<>();
        List<DataInfo> list = new ArrayList<>(100);
        for (int j = 0; j < 2; j++) {
            actual.add((long) j);
            list.add(new DataInfo(j, "junit test parameter"));
        }
        BreakUpCertainExecutorFactory<DataInfo, Void> factory = new BreakUpCertainExecutorFactory<>();
        CountNumberExecutor<DataInfo, DataInfo, Void> executor = factory.name("junit test")
                .waitTime(10)
                .data(list)
                .dataProcess(l -> {
                    try {
                        Thread.sleep(new Random().nextInt(9999));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    expect.add(l.getId());
                    return null;
                }).build();
        executor.active();
        assertEquals(actual, expect);
    }
}