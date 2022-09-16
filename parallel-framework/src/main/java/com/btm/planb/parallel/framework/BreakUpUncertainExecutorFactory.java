package com.btm.planb.parallel.framework;

import com.btm.planb.parallel.framework.model.BatchDataInfo;
import com.btm.planb.parallel.framework.model.SourceDataDefinition;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * 不固定数量任务全部打散（每批次一个数据）逐个运行的执行器工厂。<br/>
 * 适用场景：待处理的数量不固定，或无法一次性全部加载到内存中，并且每个线程一次仅需要处理一个（组）数据。<br/>
 * 限制：子线程不支持返回值
 *
 * @param <S> 数据源方法的自定义参数
 * @param <K> 数据源方法的返回值类型
 * @param <R> 数据处理方法的返回值类型
 */
public class BreakUpUncertainExecutorFactory<S, K extends SourceDataDefinition, R> extends AbstractExecutorFactory {

    /**
     * 任务全部提交完毕之后，最长等待时间(单位：秒)，超过此时间，线程池强制关闭
     */
    private int waitTime = 0;

    protected final RefreshFunction<S, K, K, R> refreshFunction = new RefreshFunction<>();

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> name(String name) {
        super.name = name;
        return this;
    }

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> parallelNumber(int number) {
        super.parallelNumber = number;
        return this;
    }

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> queueSize(int size) {
        super.queueSize = size;
        return this;
    }

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> executor(ExecutorService executorService) {
        super.executorService = executorService;
        return this;
    }

    public BreakUpUncertainExecutorFactory<S, K, R> waitTime(int waitTime) {
        this.waitTime = waitTime;
        return this;
    }

    public BreakUpUncertainExecutorFactory<S, K, R> dataSourceFunction(BiFunction<S, BatchDataInfo, List<K>> dataSourceFunction) {
        refreshFunction.setDataSourceFunction(dataSourceFunction);
        return this;
    }

    public BreakUpUncertainExecutorFactory<S, K, R> enableContinue(BiPredicate<S, BatchDataInfo> enableContinue) {
        refreshFunction.setEnableContinue(enableContinue);
        return this;
    }

    public BreakUpUncertainExecutorFactory<S, K, R> dataProcess(Function<K, R> dataProcess) {
        refreshFunction.setDataProcess(dataProcess);
        return this;
    }

    public TimeLimitExecutor<S, K, K, R> build() {
        refreshFunction.setGroup(list -> list);
        return new TimeLimitExecutor<>(name, this.waitTime, buildOneExecutor(), refreshFunction, isNeedCloseExecutor());
    }

    public WithResult withResult()  {
        return new WithResult(this.waitTime);
    }

    private class WithResult {

        private final int waitTime;

        private WithResult(int waitTime) {
            this.waitTime = waitTime;
        }

        public TimeLimitWithResultExecutor<S, K, K, R> build() {
            refreshFunction.setGroup(list -> list);
            return new TimeLimitWithResultExecutor<>(name, this.waitTime, buildOneExecutor(), refreshFunction, isNeedCloseExecutor());
        }

    }
}
