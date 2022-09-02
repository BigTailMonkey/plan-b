package com.btm.planb.db.data.refresh;


import java.util.List;
import java.util.function.Function;

public class BreakUpCertainExecutorFactory<K, R> extends AbstractExecutorFactory {

    /**
     * 任务全部提交完毕之后，最长等待时间(单位：秒，默认：600)，超过此时间，线程池强制关闭
     */
    private int waitTime = 600;

    protected final RefreshFunction<Void, K, K, R> refreshFunction = new RefreshFunction<>();

    @Override
    public BreakUpCertainExecutorFactory<K, R> name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public BreakUpCertainExecutorFactory<K, R> parallelNumber(int number) {
        super.parallelNumber(number);
        return this;
    }

    @Override
    public BreakUpCertainExecutorFactory<K, R> queueSize(int size) {
        super.queueSize(size);
        return this;
    }

    public BreakUpCertainExecutorFactory<K, R> waitTime(int waitTime) {
        this.waitTime = waitTime;
        return this;
    }

    public BreakUpCertainExecutorFactory<K, R> data(List<K> data) {
        refreshFunction.setDataSourceFunction((s, i) -> data);
        return this;
    }

    public BreakUpCertainExecutorFactory<K, R> dataProcess(Function<K, R> dataProcess) {
        refreshFunction.setDataProcess(dataProcess);
        return this;
    }

    public CountNumberExecutor<K, K, R> build() {
        refreshFunction.setGroup(list -> list);
        return new CountNumberExecutor<>(name, this.waitTime, buildOneExecutor(), refreshFunction);
    }
}
