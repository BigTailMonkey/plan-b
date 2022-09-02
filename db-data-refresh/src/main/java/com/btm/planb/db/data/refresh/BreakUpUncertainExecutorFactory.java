package com.btm.planb.db.data.refresh;

import com.btm.planb.db.data.refresh.model.BatchDataInfo;
import com.btm.planb.db.data.refresh.model.SourceDataDefinition;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class BreakUpUncertainExecutorFactory<S, K extends SourceDataDefinition, R> extends AbstractExecutorFactory {

    /**
     * 任务全部提交完毕之后，最长等待时间(单位：秒)，超过此时间，线程池强制关闭
     */
    private int waitTime = 0;

    protected final RefreshFunction<S, K, K, R> refreshFunction = new RefreshFunction<>();

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> parallelNumber(int number) {
        super.parallelNumber(number);
        return this;
    }

    @Override
    public BreakUpUncertainExecutorFactory<S, K, R> queueSize(int size) {
        super.queueSize(size);
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
        return new TimeLimitExecutor<>(name, this.waitTime, buildOneExecutor(), refreshFunction);
    }
}
