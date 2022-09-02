package com.btm.planb.db.data.refresh;

import com.btm.planb.db.data.refresh.model.BatchDataInfo;
import com.btm.planb.db.data.refresh.model.SourceDataDefinition;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public abstract class ExecutorFactory<S, K extends SourceDataDefinition, R> {

    /**
     * 日志关键词，推荐使用场景名称
     */
    protected String name = "data refresh executor";

    /**
     * 并发数量
     */
    private int parallelNumber = 2;

    /**
     * 等待队列容量
     */
    private int queueSize = 2000;

    protected ExecutorFactory name(String name) {
        this.name = name;
        return this;
    }

    protected ExecutorFactory<S, K, R> parallelNumber(int number) {
        this.parallelNumber = number;
        return this;
    }

    protected ExecutorFactory<S, K, R> queueSize(int size) {
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
