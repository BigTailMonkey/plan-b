package com.btm.planb.parallel.framework;

import com.btm.planb.parallel.framework.model.TimeLimitFutureContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 计数执行器
 * @param <K> 数据源方法的返回值类型
 * @param <Z> 待处理源数据分组方法的返回值类型
 * @param <R> 数据处理方法的返回值类型
 */
public class CountNumberWithResultExecutor<K, Z, R> extends AbstractExecutor {

    private static final Logger log = LoggerFactory.getLogger(CountNumberWithResultExecutor.class);

    private final String name;

    private final int waitTime;

    private final ExecutorService executorService;

    private final boolean needCloseExecutor;

    private final RefreshFunction<Void, K, Z, R> refreshFunction;

    protected CountNumberWithResultExecutor(String name, int waitTime, ExecutorService executorService,
                                            RefreshFunction<Void, K, Z, R> refreshFunction, boolean needCloseExecutor) {
        this.name = name;
        this.waitTime = waitTime;
        this.executorService = executorService;
        this.refreshFunction = refreshFunction;
        this.needCloseExecutor = needCloseExecutor;
    }

    public List<R> active() throws ExecutionException {
        log.debug("parallel thread framework used, {}", this.name);
        TimeLimitFutureContainer<R> container = new TimeLimitFutureContainer<>(TimeLimitFutureContainer.FutureOutTimeStrategy.DISCARDED);
        List<R> resultList;
        try {
            List<K> sourceData = refreshFunction.dataSourceFunction(null, null);
            if (Objects.nonNull(sourceData) && !sourceData.isEmpty()) {
                CountDownLatch latch = new CountDownLatch(sourceData.size());
                log.debug("task total number: {}", sourceData.size());
                List<Z> group = refreshFunction.group(sourceData);
                for (Z ks : group) {
                    container.add(executorService.submit(() -> {
                                if (Thread.interrupted()) {
                                    return null;
                                }
                                R r = refreshFunction.dataProcess(ks);
                                latch.countDown();
                                return r;
                            })
                    );
                }
                if (this.waitTime > 0) {
                    boolean await = latch.await(this.waitTime, TimeUnit.SECONDS);
                    if (!await) {
                        log.warn("[{}]count-number executor overtime close.", this.name);
                    }
                } else {
                    latch.await();
                }
            }
        } catch (InterruptedException e) {
            log.warn("[{}]count-number executor wait interrupt", this.name);
        } catch (Exception e) {
            log.error("[{}]count-number executor error.", this.name, e);
            throw e;
        } finally {
            if (needCloseExecutor) {
                safeForceShutDown(this.executorService, 0);
            }
            resultList = container.extract(5, TimeUnit.MINUTES);
        }
        return resultList;
    }



}
