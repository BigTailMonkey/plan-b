package com.btm.planb.parallel.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class TaskParallelExecutionTest {

    private final static Logger log = LoggerFactory.getLogger(TaskParallelExecutionTest.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void executeSuccess1() throws OperationNotSupportedException {
        int target = 1000000;
        Parameter parameter = new Parameter(target);
        Carrier<AtomicInteger, Parameter> carrier = new Carrier<>(new AtomicInteger(0), 10);
        for (int i = 0;i<target;i++) {
            carrier.addTask((a, b) -> {
                a.getAndIncrement();
                return d -> d.result --;
            });
        }
        TaskParallelExecution execution = new TaskParallelExecution(null);
        List<Consumer<Parameter>> execute = execution.execute(carrier);
        execute.forEach(item -> item.accept(parameter));
        assertEquals(0, parameter.result);
    }

    @Test
    public void executeSuccess2() throws OperationNotSupportedException {
        int target = 1000000;
        Carrier<AtomicInteger, Integer> carrier = new Carrier<>(new AtomicInteger(0), 10);
        for (int i = 0;i<target;i++) {
            carrier.addTask((a, b) -> {
                a.getAndIncrement();
                return d -> {
                    assertEquals((int)d, a.get());
                };
            });
        }
        TaskParallelExecution execution = new TaskParallelExecution(null);
        List<Consumer<Integer>> execute = execution.execute(carrier);
        execute.forEach(item -> item.accept(target));
    }

    @Test
    public void executeException() throws OperationNotSupportedException {
        int target = 100;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 1, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(10000), new ThreadPoolExecutor.CallerRunsPolicy());
        Carrier<AtomicInteger, Integer> carrier = new Carrier<>(new AtomicInteger(0), 10);
        for (int i = 0;i<target;i++) {
            carrier.addTask((a, b) -> {
                log.info("刚刚进入方法：{}", a.get());
                if (Thread.currentThread().isInterrupted()) {
                    log.info("我收到了中断信号：{}", a.get());
                    throw new RuntimeException("中断");
                }
                try {
                    Thread.sleep(new Random().nextInt(999));
                } catch (InterruptedException e) {
                    log.info("我在休眠中收到了中断信号：{}", a.get());
                    throw new RuntimeException("中断");
                }
                log.info("刚刚休眠完：{}", a.get());
                a.getAndIncrement();
                if (a.get() > target * 0.8) {
                    log.info("{}", a.get());
                    throw new RuntimeException(Thread.currentThread().getName() + "随机抛出一个异常");
                }
                return d -> {
                    assertEquals((int)d, a.get());
                };
            });
        }
        TaskParallelExecution execution = new TaskParallelExecution(executor);
        expectedException.expect(ParallelTaskException.class);
        execution.execute(carrier);
    }

    private static class Parameter {
        int result;
        public Parameter(int result) {
            this.result = result;
        }
    }
}