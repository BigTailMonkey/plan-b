package com.btm.planb.parallel.framework;

import com.btm.planb.parallel.framework.model.BatchDataInfo;
import com.btm.planb.parallel.framework.model.SourceDataDefinition;
import com.btm.planb.parallel.framework.model.TimeLimitFutureContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 限时执行器
 * @param <S> 数据源方法的自定义参数
 * @param <K> 数据源方法的返回值类型
 * @param <Z> 待处理源数据分组方法的返回值类型
 * @param <R> 数据处理方法的返回值类型
 */
public class TimeLimitWithResultExecutor<S, K extends SourceDataDefinition, Z, R> extends AbstractExecutor {

    private static final Logger log = LoggerFactory.getLogger(TimeLimitWithResultExecutor.class);

    private final String name;

    private final int waitTime;

    private final ExecutorService executorService;

    private final boolean needCloseExecutor;

    private final RefreshFunction<S, K, Z, R> refreshFunction;

    protected TimeLimitWithResultExecutor(String name, int waitTime, ExecutorService executorService,
                                          RefreshFunction<S, K, Z, R> refreshFunction, boolean needCloseExecutor) {
        this.name = name;
        this.waitTime = waitTime;
        this.executorService = executorService;
        this.refreshFunction = refreshFunction;
        this.needCloseExecutor = needCloseExecutor;
    }

    public List<R> active(S s) throws ExecutionException {
        log.info("time-limit executor start, {}", this.name);
        BatchDataInfo batchDataInfo;
        boolean enableContinue;
        TimeLimitFutureContainer<R> container = new TimeLimitFutureContainer<>(TimeLimitFutureContainer.FutureOutTimeStrategy.DISCARDED);
        List<R> resultList;
        try {
            do {
                batchDataInfo = new BatchDataInfo();
                List<K> sourceData = refreshFunction.dataSourceFunction(s, batchDataInfo);
                if (Objects.nonNull(sourceData) && !sourceData.isEmpty()) {
                    List<Z> group = refreshFunction.group(sourceData);
                    for (Z zs : group) {
                        Future<R> submit = executorService.submit(() -> {
                            if (Thread.interrupted()) {
                                return null;
                            }
                            return refreshFunction.dataProcess(zs);
                        });
                        container.add(submit);
                    }
                    batchDataInfo.setMaxId(sourceData.stream().map(SourceDataDefinition::getId).max(Long::compareTo).orElse(batchDataInfo.getMaxId()));
                    batchDataInfo.setCountNumber(sourceData.size());
                }
                enableContinue = refreshFunction.enableContinue(s, batchDataInfo);
            } while (enableContinue);
        } catch (Exception e) {
            log.error("time-limit executor error.", e);
            throw e;
        } finally {
            if (needCloseExecutor) {
                safeForceShutDown(this.executorService, this.waitTime);
            }
            resultList = container.extract(1, TimeUnit.MINUTES);
        }
        return resultList;
    }



}
