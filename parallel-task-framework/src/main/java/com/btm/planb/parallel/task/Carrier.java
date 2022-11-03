package com.btm.planb.parallel.task;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * 任务集
 * @param <V> 任务集中任务执行时使用的参数类型
 * @param <T> 任务集中任务执行完毕后返回的动作运行时使用的参数类型
 */
public class Carrier<V, T> {

    // 一组任务为相关性极近的动作，所有行为可以使用同一个参数对象
    private final V parameter;
    // 任务集
    private final List<BiFunction<V, Boolean, Consumer<T>>> tasks = new ArrayList<>();
    // 是否已经开始执行任务集中的任务
    private volatile boolean taskRunning = false;
    // 任务集中是否有任务发生了异常
    private volatile boolean hasException = false;
    // 捕获到的异常
    private volatile Exception exception;
    // 当前这一批任务中，单个任务的最长等待时间
    private final int waitTime;

    public Carrier(V parameter, int waitTime) {
        this.parameter = parameter;
        this.waitTime = waitTime;
    }

    public V getParameter() {
        return this.parameter;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    /**
     * 向任务集添加任务，在添加完任务前请勿调用{@link Carrier#doAllTask()}方法，否则将无法再添加新任务。
     * @param task 任务动作，
     *             第一个类型参数：方法执行的公用参数对象；
     *             第二个类型参数：当前任务集中是否有任务发生了异常，请在执行长耗时动作前检查此参数是否为false或线程的中断信号；
     *             第三个类型参数：任务执行完后需要返回主线程执行的动作
     * @throws OperationNotSupportedException 调用{@link Carrier#doAllTask()}方法开始执行任务后再调用此方法时抛出
     */
    public void addTask(BiFunction<V, Boolean, Consumer<T>> task) throws OperationNotSupportedException {
        if (this.taskRunning) {
            throw new OperationNotSupportedException("任务已开始执行，禁止添加新任务");
        }
        tasks.add(task);
    }

    /**
     * 开始执行全部任务
     * @return 调用@{link Carrier#addTask(java.util.function.BiFunction)}方法添加进去的任务
     */
    protected List<BiFunction<V, Boolean, Consumer<T>>> doAllTask() {
        this.taskRunning = true;
        return this.tasks;
    }

    /**
     * 任务集是否有任务发生了异常
     * @return true——是；false——否
     */
    public boolean hasException() {
        return this.hasException;
    }

    /**
     * 设置异常信息
     * @param e 任务执行过程中捕获的异常
     */
    protected void setException(Exception e) {
        if (Objects.isNull(exception)) {
            this.exception = e;
        }
        this.hasException = true;
    }

    protected Exception getException() {
        return this.exception;
    }
}
