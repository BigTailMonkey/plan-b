package com.btm.planb.db.data.refresh;

import com.btm.planb.db.data.refresh.model.BatchDataInfo;
import com.btm.planb.db.data.refresh.model.SourceDataDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * 限时执行器
 * @param <S> 数据源方法的自定义参数
 * @param <K> 数据源方法的返回值类型
 */
public class TimeLimitExecutor<S, K extends SourceDataDefinition, R> extends AbstractExecutor {

    private static final Logger log = LoggerFactory.getLogger(TimeLimitExecutor.class);

    private final String name;

    private final int waitTime;

    private final ExecutorService executorService;

    private final RefreshFunction<S, K, R> refreshFunction;

    protected TimeLimitExecutor(String name, int waitTime, ExecutorService executorService, RefreshFunction<S, K, R> refreshFunction) {
        this.name = name;
        this.waitTime = waitTime;
        this.executorService = executorService;
        this.refreshFunction = refreshFunction;
    }

    public void active(S s) {
        log.info("db data refresh start, {}", this.name);
        BatchDataInfo batchDataInfo;
        boolean enableContinue;
        do {
            batchDataInfo = new BatchDataInfo();
            List<K> sourceData = refreshFunction.dataSourceFunction(s, batchDataInfo);
            if (Objects.nonNull(sourceData) && !sourceData.isEmpty()) {
                List<List<K>> group = refreshFunction.group(sourceData);
                for (List<K> ks : group) {
                    executorService.execute(() -> refreshFunction.dataProcess(ks));
                }
                batchDataInfo.setMaxId(sourceData.stream().map(SourceDataDefinition::getId).max(Long::compareTo).orElse(batchDataInfo.getMaxId()));
                batchDataInfo.setCountNumber(sourceData.size());
            }
            enableContinue = refreshFunction.enableContinue(s, batchDataInfo);
        } while (enableContinue);
        safeForceShutDown(this.executorService, this.waitTime);
    }



}
