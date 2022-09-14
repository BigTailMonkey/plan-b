package com.btm.planb.parallel.framework;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * 固定数量任务全部打散（每批次一个数据）逐个运行的执行器工厂。<br/>
 * 适用场景：待处理的数量固定且较少，且可一次性全部加载到内存中，并且每个线程一次仅需要处理一个（组）数据。<br/>
 * 限制：子线程不支持返回值
 *
 * @param <K> 数据源方法的返回值类型
 * @param <R> 数据处理方法的返回值类型
 */
public class BreakUpCertainExecutorFactory<K, R> extends AbstractExecutorFactory {

    /**
     * 任务全部提交完毕之后，最长等待时间(单位：秒，默认：600)，超过此时间，线程池强制关闭
     */
    private int waitTime = 600;

    protected final RefreshFunction<Void, K, K, R> refreshFunction = new RefreshFunction<>();

    @Override
    public BreakUpCertainExecutorFactory<K, R> name(String name) {
        super.name = name;
        return this;
    }

    @Override
    public BreakUpCertainExecutorFactory<K, R> parallelNumber(int number) {
        super.parallelNumber = number;
        return this;
    }

    @Override
    public BreakUpCertainExecutorFactory<K, R> queueSize(int size) {
        super.queueSize = size;
        return this;
    }

    @Override
    protected BreakUpCertainExecutorFactory<K, R> executor(ExecutorService executorService) {
        super.executorService = executorService;
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

    public WithResult withResult()  {
        return new WithResult(this.waitTime);
    }

    public class WithResult {

        private final int waitTime;

        private WithResult(int waitTime) {
            this.waitTime = waitTime;
        }

        public CountNumberWithResultExecutor<K, K, R> build() {
            refreshFunction.setGroup(list -> list);
            return new CountNumberWithResultExecutor<>(name, this.waitTime, buildOneExecutor(), refreshFunction);
        }

    }
}
