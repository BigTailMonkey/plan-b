package com.btm.planb.db.data.refresh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExecutor {


    /**
     * 安全强制关闭线程池，最长等待时间内任务全部执行完毕则关闭线程池，若超过最长等待时间依旧未完成，则强制关闭
     * @param executorService 线程池对象
     * @param waitTime 最长等待时间，单位：秒
     */
    protected void safeForceShutDown(ExecutorService executorService, int waitTime) {
        if (waitTime > 0) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                    // 超过了等待时间但线程池依旧没有关闭，就强制关闭
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } else {
            executorService.shutdownNow();
        }
    }
}
