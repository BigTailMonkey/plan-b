package com.btm.planb.parallel.thread.transaction;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;


@FunctionalInterface
public interface ThreadAction<T> {

    TransactionSynchronizationAdapter action(T t);

}
