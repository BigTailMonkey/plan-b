package com.btm.planb.parallel.task;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 执行器，
 * 维护一个线程池，用来执行一个线程组的全部的任务
 */
public class Actuator {

    private final ExecutorService executor;

    public Actuator(ExecutorService executor) {
        if (Objects.isNull(executor)) {
            this.executor = new ThreadPoolExecutor(8, 16, 1, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<>(10000), new ThreadPoolExecutor.CallerRunsPolicy());
        } else {
            this.executor = executor;
        }
    }

    public void execute(AbstractTaskThread abstractTaskThread) {
        this.executor.execute(abstractTaskThread);
    }

}
