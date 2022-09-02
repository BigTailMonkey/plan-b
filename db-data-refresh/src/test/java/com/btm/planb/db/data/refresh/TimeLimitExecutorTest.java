package com.btm.planb.db.data.refresh;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TimeLimitExecutorTest {

    @Test
    public void activeByFlexibleExecutor() {
        Set<Long> actual = new HashSet<>();
        Set<Long> expect = new HashSet<>();
        TestParameter testParameter = new TestParameter();
        testParameter.setName("junit test parameter");
        FlexibleExecutorFactory<TestParameter, DataInfo, Void> factory = new FlexibleExecutorFactory<>();
        TimeLimitExecutor<TestParameter, DataInfo, List<DataInfo>, Void> executor = factory.name("junit test")
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
                })
                .group(list -> list.stream().map(Collections::singletonList).collect(Collectors.toList()))
                .dataProcess(l -> {
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

    @Test
    public void activeByBreakUpExecutor() {
        Set<Long> actual = new HashSet<>();
        Set<Long> expect = new HashSet<>();
        TestParameter testParameter = new TestParameter();
        testParameter.setName("junit test parameter");
        BreakUpUncertainExecutorFactory<TestParameter, DataInfo, Void> factory = new BreakUpUncertainExecutorFactory<>();
        TimeLimitExecutor<TestParameter, DataInfo, DataInfo, Void> executor = factory.name("junit test")
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
                })
                .dataProcess(l -> {
                    try {
                        Thread.sleep(new Random().nextInt(9999));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    expect.add(l.getId());
                    return null;
                }).build();
        executor.active(testParameter);
        assertEquals(actual, expect);
    }
}