package com.btm.planb.parallel.framework;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TimeLimitWithResultExecutorTest {

    @Test
    public void activeByFlexibleExecutor() throws ExecutionException {
        Set<Long> actual = new HashSet<>();
        TestParameter testParameter = new TestParameter();
        testParameter.setName("junit test parameter");
        FlexibleWithResultExecutorFactory<TestParameter, DataInfo, Long> factory = new FlexibleWithResultExecutorFactory<>();
        TimeLimitWithResultExecutor<TestParameter, DataInfo, List<DataInfo>, Long> executor = factory.name("junit test")
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
                    return dataInfo.getId();
                }).build();
        List<Long> active = executor.active(testParameter);
        Set<Long> expect = new HashSet<>(active);
        assertEquals(actual, expect);
    }

    @Test
    public void activeByFlexibleExecutorInterrupted() throws ExecutionException {
        Set<Long> expect = Collections.singleton(-1L);
        TestParameter testParameter = new TestParameter();
        testParameter.setName("junit test parameter");
        FlexibleWithResultExecutorFactory<TestParameter, DataInfo, Long> factory = new FlexibleWithResultExecutorFactory<>();
        TimeLimitWithResultExecutor<TestParameter, DataInfo, List<DataInfo>, Long> executor = factory.name("junit test")
                .waitTime(5)
                .enableContinue((p, b) -> b.getMaxId() < 1)
                .dataSourceFunction((p, i) -> {
                    if (i.getMaxId() > 0) {
                        return Collections.emptyList();
                    }
                    List<DataInfo> list = new ArrayList<>(100);
                    for (int j = 0; j < 2; j++) {
                        list.add(new DataInfo(j, p.getName()));
                    }
                    return list;
                })
                .group(list -> list.stream().map(Collections::singletonList).collect(Collectors.toList()))
                .dataProcess(l -> {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        return -1L;
                    }
                    DataInfo dataInfo = l.get(0);
                    return dataInfo.getId();
                }).build();
        List<Long> active = executor.active(testParameter);
        Set<Long> actual = new HashSet<>(active);
        assertEquals(expect, actual);
    }

    @Test
    public void activeByFlexibleExecutorInterrupted2() throws ExecutionException {
        TestParameter testParameter = new TestParameter();
        testParameter.setName("junit test parameter");
        FlexibleWithResultExecutorFactory<TestParameter, DataInfo, Long> factory = new FlexibleWithResultExecutorFactory<>();
        TimeLimitWithResultExecutor<TestParameter, DataInfo, List<DataInfo>, Long> executor = factory.name("junit test")
                .waitTime(10)
                .parallelNumber(2)
                .enableContinue((p, b) -> b.getMaxId() < 1)
                .dataSourceFunction((p, i) -> {
                    if (i.getMaxId() > 100) {
                        return Collections.emptyList();
                    }
                    List<DataInfo> list = new ArrayList<>(100);
                    for (int j = 0; j < 2; j++) {
                        list.add(new DataInfo(j, p.getName()));
                    }
                    return list;
                })
                .group(list -> list.stream().map(Collections::singletonList).collect(Collectors.toList()))
                .dataProcess(l -> {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        // do nothing....
                        // keep the thread running.
                    }
                    DataInfo dataInfo = l.get(0);
                    return dataInfo.getId();
                }).build();
        List<Long> active = executor.active(testParameter);
        Set<Long> actual = new HashSet<>(active);
        Set<Long> expect = new HashSet<>(Arrays.asList(0L, 1L));
        assertEquals(expect, actual);
    }
}