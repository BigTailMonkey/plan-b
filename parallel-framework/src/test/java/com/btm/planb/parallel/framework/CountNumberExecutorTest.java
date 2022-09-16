package com.btm.planb.parallel.framework;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class CountNumberExecutorTest {

    private final Logger logger = LoggerFactory.getLogger(CountNumberExecutorTest.class);

    @Test
    public void activeSleep() {
        Set<Long> actual = new HashSet<>();
        Set<Long> expect = new HashSet<>();
        List<DataInfo> list = new ArrayList<>(100);
        for (int j = 0; j < 2; j++) {
            actual.add((long) j);
            list.add(new DataInfo(j, "junit test parameter"));
        }
        BreakUpCertainExecutorFactory<DataInfo, Void> factory = new BreakUpCertainExecutorFactory<>();
        CountNumberExecutor<DataInfo, DataInfo, Void> executor = factory.name("junit-test-thread")
                .waitTime(10)
                .data(list)
                .dataProcess(l -> {
                    try {
                        Thread.sleep(new Random().nextInt(9999));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    expect.add(l.getId());
                    logger.debug("sub thread[{}] run end.", l.getId());
                    return null;
                }).build();
        executor.active();
        assertEquals(actual, expect);
        logger.debug("junit test finish.");
    }

    @Test
    public void activeNoSleep() {
        Set<Long> actual = new HashSet<>();
        Set<Long> expect = new HashSet<>();
        List<DataInfo> list = new ArrayList<>(100);
        for (int j = 0; j < 2; j++) {
            actual.add((long) j);
            list.add(new DataInfo(j, "junit test parameter"));
        }
        BreakUpCertainExecutorFactory<DataInfo, Void> factory = new BreakUpCertainExecutorFactory<>();
        CountNumberExecutor<DataInfo, DataInfo, Void> executor = factory.name("junit-test-thread")
                .waitTime(10)
                .data(list)
                .dataProcess(l -> {
                    expect.add(l.getId());
                    logger.debug("sub thread[{}] run end.", l.getId());
                    return null;
                }).build();
        executor.active();
        assertEquals(actual, expect);
        logger.debug("junit test finish.");
    }


    @Test
    public void activeBySameExecutor() {
        ExecutorService executorService = new ThreadPoolExecutor(256, 256,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000000),
                new ThreadPoolExecutor.CallerRunsPolicy());
        Set<String> actual = new ConcurrentSkipListSet<>();
        Set<String> expect = new ConcurrentSkipListSet<>();
        List<List<DataInfo>> list = new ArrayList<>(1000);
        for (int k = 0;k < 1000;k++) {
            List<DataInfo> subList = new ArrayList<>(100);
            for (int j = 0; j < 100; j++) {
                actual.add(k + "_" + j);
                subList.add(new DataInfo(j, k + "_" + j));
            }
            list.add(subList);
        }
        BreakUpCertainExecutorFactory<List<DataInfo>, Void> factory = new BreakUpCertainExecutorFactory<>();
        CountNumberExecutor<List<DataInfo>, List<DataInfo>, Void> executor = factory.name("junit-test-thread")
                .executor(executorService)
                .waitTime(Integer.MAX_VALUE)
                .data(list)
                .dataProcess(l -> {
                    l.forEach(a -> {
                        expect.add(a.getName());
                        logger.debug("sub thread[{}] run end.", a.getName());
                    });
                    return null;
                }).build();
        executor.active();
        assertEquals(actual, expect);
        logger.debug("junit test finish.");
        executorService.shutdown();
    }
}