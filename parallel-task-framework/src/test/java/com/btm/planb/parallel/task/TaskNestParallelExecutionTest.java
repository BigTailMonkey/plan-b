package com.btm.planb.parallel.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * 请不要在使用同一线程池的情况下，对Carrier进行嵌套创建。
 * 即：不能在Carrier管理的子任务中创建新的Carrier并与父Carrier使用同一线程池运行。
 * 此种情况易发生线程池中的线程相互等待的问题，造成死锁。特别是在外部carrier被大并发调用创建时，此种死锁情况更易发生。
 *
 * 若要对Carrier嵌套使用，请为内部的Carrier提供单独的线程池。
 */
public class TaskNestParallelExecutionTest {

    private static final Logger log = LoggerFactory.getLogger(TaskParallelExecutionTest.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    /**
     * 正确示例：
     * 嵌套使用Carrier时，需做好内外Carrier的线程资源隔离
     * @throws OperationNotSupportedException
     */
    @Test
    public void nestExample() throws OperationNotSupportedException {
        int target = 100;
        Parameter parameter = new Parameter(target);
        Carrier<AtomicInteger, Parameter> carrier = new Carrier<>(new AtomicInteger(0), 30);
        TaskParallelExecution execution = new TaskParallelExecution(null);
        TaskParallelExecution nestExecution = new TaskParallelExecution(null);
        for (int i = 0;i<target/10;i++) {
            carrier.addTask((a, b) -> {
                log.info("外部任务添加进度：{}", a);
                Carrier<AtomicInteger, Parameter> nestCarrier = new Carrier<>(a, 10);
                for (int j = 0;j < 10;j++) {
                    try {
                        nestCarrier.addTask((c, e) -> {
                            c.getAndIncrement();
                            return d -> d.result--;
                        });
                    } catch (OperationNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                List<Consumer<Parameter>> nestExecute = nestExecution.execute(nestCarrier);
                nestExecute.forEach(item -> item.accept(parameter));
                log.info("内部任务全部执行完毕进度：{}", a);
                return e -> {};
            });
        }
        List<Consumer<Parameter>> execute = execution.execute(carrier);
        execute.forEach(item -> item.accept(parameter));
        assertEquals(0, parameter.result);
    }


    /**
     * 错误示例：
     * carrier创建的线程会沾满线程资源，且一直等待carrier中的线程创建的nestCarrier执行完毕。
     * 但carrier和nestCarrier使用同一个线程池，carrier沾满了线程资源，nestCarrier分配不到任何线程资源。
     * 最终carrier的线程等待超时而结束。
     * @throws OperationNotSupportedException
     */
    @Test
    public void nestErrorExample() throws OperationNotSupportedException {
        int target = 100;
        Parameter parameter = new Parameter(target);
        Carrier<AtomicInteger, Parameter> carrier = new Carrier<>(new AtomicInteger(0), 20);
        TaskParallelExecution execution = new TaskParallelExecution(null);
        for (int i = 0;i<target/10;i++) {
            carrier.addTask((a, b) -> {
                log.info("外部任务添加进度：{}", a);
                Carrier<AtomicInteger, Parameter> nestCarrier = new Carrier<>(a, 10);
                for (int j = 0;j < 10;j++) {
                    try {
                        nestCarrier.addTask((c, e) -> {
                            log.info("内部任务添加进度：{}", a);
                            c.getAndIncrement();
                            return d -> d.result--;
                        });
                    } catch (OperationNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                List<Consumer<Parameter>> execute = execution.execute(nestCarrier);
                execute.forEach(item -> item.accept(parameter));
                return e -> {};
            });
        }
        expectedException.expect(ParallelTaskException.class);
        List<Consumer<Parameter>> execute = execution.execute(carrier);
        execute.forEach(item -> item.accept(parameter));
    }

    private static class Parameter {
        int result;
        public Parameter(int result) {
            this.result = result;
        }
    }

}
