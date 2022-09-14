package com.btm.planb.parallel.thread.transaction;


import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

public class ThreadManager<T> {

    private final ExecutorService executorService;

    public ThreadManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void addAction(T t, Function<T, TransactionSynchronizationAdapter> function) throws ExecutionException, InterruptedException {
        Future<TransactionSynchronizationAdapter> submit = this.executorService.submit(() -> function.apply(t));
        TransactionSynchronizationManager.registerSynchronization(submit.get());
    }

}
