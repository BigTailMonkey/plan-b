package com.btm.planb.parallel.framework.model;


import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface FutureContainer<V> {

    void add(Future<V> vFuture);

    List<V> extract(Integer time, TimeUnit timeUnit) throws Exception;

}
