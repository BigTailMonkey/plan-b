package com.btm.planb.db.data.refresh;

import com.btm.planb.db.data.refresh.model.BatchDataInfo;
import com.btm.planb.db.data.refresh.model.SourceDataDefinition;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RefreshFunction<S, K extends SourceDataDefinition, R> {


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
    private Function<List<K>, List<List<K>>> group;

    /**
     * 数据处理方法
     */
    private Function<List<K>, List<R>> dataProcess;

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

    public List<List<K>> group(List<K> list) {
        return Objects.isNull(this.group) ? list.parallelStream().map(Collections::singletonList).collect(Collectors.toList()) : group.apply(list);
    }

    public void setGroup(Function<List<K>, List<List<K>>> group) {
        this.group = group;
    }

    public  List<R> dataProcess(List<K> list) {
        return dataProcess.apply(list);
    }

    public void setDataProcess(Function<List<K>, List<R>> dataProcess) {
        this.dataProcess = dataProcess;
    }
}
