package com.btm.planb.parallel.framework.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

public final class TimeLimitFutureContainer<V> implements FutureContainer<V> {

    private final Logger logger = LoggerFactory.getLogger(TimeLimitFutureContainer.class);

    private final Queue<Element<V>> futures = new ConcurrentLinkedQueue<>();
    private final FutureOutTimeStrategy futureOutTimeStrategy;

    public TimeLimitFutureContainer(FutureOutTimeStrategy strategy) {
        this.futureOutTimeStrategy = strategy;
    }

    @Override
    public void add(Future<V> future) {
        this.futures.add(new Element<>(future));
    }

    public List<V> extract() throws ExecutionException, InterruptedException {
        return this.extract(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    /**
     * 提取future中的数据，如果一个future还未被调度则不会调取其get方法，被调度后才会调用其get方法等待数据返回。
     *
     * @param time 单个future等待的最长时间
     * @param timeUnit 单个future等待的时间单位
     *
     * @throws ExecutionException
     */
    @Override
    public List<V> extract(Integer time, TimeUnit timeUnit) throws ExecutionException {
        List<V> resultList = new ArrayList<>();
        do {
            try {
                Element<V> element = this.futures.poll();
                if (Objects.isNull(element)) {
                    break;
                }
                Future<V> one = element.future;
                if (one.isCancelled()) {
                    continue;
                } else if (one.isDone()) {
                    if (Thread.interrupted()) {
                        break;
                    }
                    if (Objects.nonNull(time)) {
                        resultList.add(one.get(time, timeUnit));
                    } else {
                        resultList.add(one.get());
                    }
                } else {
                    this.futures.add(element);
                }
            } catch (TimeoutException e) {
                if (FutureOutTimeStrategy.DISCARDED.equals(futureOutTimeStrategy)) {
                    logger.warn("sub-thread future get timeout.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        } while (!futures.isEmpty());
        return resultList;
    }


    private static class Element<V> {
        private final Future<V> future;
        private final long createDate;

        public Element(Future<V> future) {
            this.createDate = System.currentTimeMillis();
            this.future = future;
        }

        public Future<V> getFuture() {
            return future;
        }

        public long getCreateDate() {
            return createDate;
        }
    }

    /**
     * future的线程发生中断或超时时如何处理
     */
    public enum FutureOutTimeStrategy {
        THROW_EXCEPTION, DISCARDED
    }

}
