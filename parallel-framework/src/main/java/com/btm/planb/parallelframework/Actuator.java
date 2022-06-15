package com.btm.planb.parallelframework;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 执行器
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
