package com.btm.planb.parallel.framework;

import com.btm.planb.parallel.framework.model.BatchDataInfo;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * 过程方法定义
 *
 * @param <S> 数据源方法的自定义参数
 * @param <K> 数据源方法的返回值类型
 * @param <Z> 待处理源数据分组方法的返回值类型
 * @param <R> 数据处理方法的返回值类型
 */
public class RefreshFunction<S, K, Z, R> {


    /**
     * 数据源方法，提供数据刷新要操作的数据
     */
    private BiFunction<S, BatchDataInfo, List<K>> dataSourceFunction;

    /**
     * 继续处理判定方法<br/>
     * 返回true-继续提取并处理后续数据；false-中止执行
     */
    private BiPredicate<S, BatchDataInfo> enableContinue;

    /**
     * 待处理源数据分组方法
     */
    private Function<List<K>, List<Z>> group;

    /**
     * 数据处理方法
     */
    private Function<Z, R> dataProcess;

    protected RefreshFunction() {}


    public List<K> dataSourceFunction(S s, BatchDataInfo info) {
        return dataSourceFunction.apply(s, info);
    }

    public void setDataSourceFunction(BiFunction<S, BatchDataInfo, List<K>> dataSourceFunction) {
        this.dataSourceFunction = dataSourceFunction;
    }

    public Boolean enableContinue(S s, BatchDataInfo info) {
        Objects.requireNonNull(this.enableContinue);
        return enableContinue.test(s,  info);
    }

    public void setEnableContinue(BiPredicate<S, BatchDataInfo> enableContinue) {
        this.enableContinue = enableContinue;
    }

    public List<Z> group(List<K> list) {
        return group.apply(list);
    }

    public void setGroup(Function<List<K>, List<Z>> group) {
        this.group = group;
    }

    public  R dataProcess(Z list) {
        return dataProcess.apply(list);
    }

    public void setDataProcess(Function<Z, R> dataProcess) {
        this.dataProcess = dataProcess;
    }
}
