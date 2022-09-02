package com.btm.planb.db.data.refresh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExecutorFactory {

    /**
     * 日志关键词，推荐使用场景名称
     */
    protected String name = "data refresh executor";

    /**
     * 并发数量，默认为2
     */
    private int parallelNumber = 2;

    /**
     * 等待队列容量，默认2000
     */
    private int queueSize = 2000;

    protected AbstractExecutorFactory name(String name) {
        this.name = name;
        return this;
    }

    protected AbstractExecutorFactory parallelNumber(int number) {
        this.parallelNumber = number;
        return this;
    }

    protected AbstractExecutorFactory queueSize(int size) {
        this.queueSize = size;
        return this;
    }

    protected ExecutorService buildOneExecutor() {
        return new ThreadPoolExecutor(
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
