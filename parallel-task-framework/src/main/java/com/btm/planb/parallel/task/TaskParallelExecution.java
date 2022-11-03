package com.btm.planb.parallel.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 任务并发执行器<br/>
 * 初始时需要传入一个线程池，用于执行任务集中的任务。<br/>
 * 建议此执行器在全局中使用单一实例。
 */

public class TaskParallelExecution {

    private static final Logger log = LoggerFactory.getLogger(TaskParallelExecution.class);

    private final ExecutorService executor;

    public TaskParallelExecution(ExecutorService executor) {
        if (Objects.isNull(executor)) {
            this.executor = new ThreadPoolExecutor(8, 16, 1, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<>(10000), new ThreadPoolExecutor.CallerRunsPolicy());
        } else {
            this.executor = executor;
        }
    }

    public <V, T> List<Consumer<T>> execute(Carrier<V, T> carrier) {
        // 检查任务集是否有效
        List<BiFunction<V, Boolean, Consumer<T>>> allTasks = carrier.doAllTask();
        if (Objects.isNull(allTasks) || allTasks.isEmpty()) {
            return Collections.emptyList();
        }
        // 提交任务集中的全部任务到执行器中去执行
        List<Future<Consumer<T>>> allResults =
                allTasks.stream().map(task -> this.executor.submit(() -> {
                    if (carrier.hasException() || Thread.currentThread().isInterrupted()) {
                        if (log.isDebugEnabled()) {
                            log.debug("收到中断信号或任务集发生了异常");
                        }
                        throw new InterruptedException("任务组发生异常，任务全部中止");
                    }
                    return task.apply(carrier.getParameter(), carrier.hasException());
                })).collect(Collectors.toList());
        if (log.isDebugEnabled()) {
            log.debug("添加任务完成：{}", allTasks.size());
        }
        // 提取执行结果
        List<Consumer<T>> results = new ArrayList<>(allResults.size());
        for (Future<? extends Consumer<T>> result : allResults) {
            try {
                // 任务集中有任务发生异常且当前任务状态为未取消，则直接可中断的取消当前任务
                if (carrier.hasException() && !result.isCancelled()) {
                    result.cancel(true);
                } else if (!result.isCancelled()) {
                    // 获取任务的返回结果，若发生异常或超时，则更新任务集的异常状态，并记录异常描述信息
                    results.add(result.get(carrier.getWaitTime(), TimeUnit.SECONDS));
                }
                if (log.isDebugEnabled() && result.isCancelled()){
                    log.debug("{}已被取消", result);
                }
            } catch (Exception e) {
                carrier.setException(e);
            }
        }
        if (carrier.hasException()) {
            throw new ParallelTaskException(carrier.getException());
        }
        return results;
    }

}
