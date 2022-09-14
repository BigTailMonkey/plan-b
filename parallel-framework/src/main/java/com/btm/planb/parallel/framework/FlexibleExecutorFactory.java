package com.btm.planb.parallel.framework;

import com.btm.planb.parallel.framework.model.BatchDataInfo;
import com.btm.planb.parallel.framework.model.SourceDataDefinition;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * 灵活执行器工厂。<br/>
 * 任务加载、分组限制少，灵活性高。<br/>
 * 适用场景：需要处理大量数据（无法一次性全部加载到内存中），且每个线程一次需要处理多个数据。<br/>
 * 限制：子线程不支持返回值，要支持返回值可使用{@link FlexibleWithResultExecutorFactory}
 *
 * @param <S> 数据源方法的自定义参数
 * @param <K> 数据源方法的返回值类型
 */
public class FlexibleExecutorFactory<S, K extends SourceDataDefinition> extends AbstractExecutorFactory {

    /**
     * 任务全部提交完毕之后，最长等待时间(单位：秒)，超过此时间，线程池强制关闭
     */
    private int waitTime = 1;

    protected final RefreshFunction<S, K, List<K>, Void> refreshFunction = new RefreshFunction<>();

    @Override
    public FlexibleExecutorFactory<S, K> name(String name) {
        super.name = name;
        return this;
    }

    @Override
    public FlexibleExecutorFactory<S, K> parallelNumber(int number) {
        super.parallelNumber = number;
        return this;
    }

    @Override
    public FlexibleExecutorFactory<S, K> queueSize(int size) {
        super.queueSize = size;
        return this;
    }

    @Override
    public FlexibleExecutorFactory<S, K> executor(ExecutorService executorService) {
        super.executorService = executorService;
        return this;
    }

    public FlexibleExecutorFactory<S, K> waitTime(int waitTime) {
        this.waitTime = waitTime;
        return this;
    }

    public FlexibleExecutorFactory<S, K> dataSourceFunction(BiFunction<S, BatchDataInfo, List<K>> dataSourceFunction) {
        refreshFunction.setDataSourceFunction(dataSourceFunction);
        return this;
    }

    public FlexibleExecutorFactory<S, K> enableContinue(BiPredicate<S, BatchDataInfo> enableContinue) {
        refreshFunction.setEnableContinue(enableContinue);
        return this;
    }

    public FlexibleExecutorFactory<S, K> group(Function<List<K>, List<List<K>>> group) {
        refreshFunction.setGroup(group);
        return this;
    }

    public FlexibleExecutorFactory<S, K> dataProcess(Function<List<K>, Void> dataProcess) {
        refreshFunction.setDataProcess(dataProcess);
        return this;
    }

    public TimeLimitExecutor<S, K, List<K>, Void> build() {
        return new TimeLimitExecutor<>(name, this.waitTime, buildOneExecutor(), refreshFunction);
    }
    
}
