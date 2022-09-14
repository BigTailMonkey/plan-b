package com.btm.planb.parallel.framework;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 执行器工厂抽象
 */
public abstract class AbstractExecutorFactory {

    /**
     * 日志关键词，推荐使用场景名称
     */
    protected String name = "data refresh executor";

    /**
     * 并发数量，默认为2
     */
    protected int parallelNumber = 2;

    /**
     * 等待队列容量，默认2000
     */
    protected int queueSize = 2000;

    protected ExecutorService executorService;

    protected abstract AbstractExecutorFactory name(String name);

    protected abstract AbstractExecutorFactory parallelNumber(int number);

    protected abstract AbstractExecutorFactory queueSize(int size);

    protected abstract AbstractExecutorFactory executor(ExecutorService executorService);

    protected ExecutorService buildOneExecutor() {

        return Objects.nonNull(executorService) ? executorService : new ThreadPoolExecutor(
                this.parallelNumber, this.parallelNumber,
                0, TimeUnit.MILLISECONDS
                , new LinkedBlockingQueue<>(this.queueSize),
                r -> {
                    Thread thread = new Thread(r, name);
                    thread.setDaemon(true);
                    return thread;
                },
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
