package com.btm.planb.parallel.thread.transaction;

import com.btm.planb.parallel.framework.BreakUpCertainExecutorFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * 依赖于Spring的多线程公用事务的线程管理器。<br/>
 *
 * @param <T> 并行线程执行方法的入参
 */
public class SpringTransactionThreadManager<T> {

    private final ExecutorService executorService;

    public SpringTransactionThreadManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void addAction(List<Function<T, TransactionSynchronizationAdapter>> functions, T t) throws ExecutionException {
        BreakUpCertainExecutorFactory<Function<T, TransactionSynchronizationAdapter>, TransactionSynchronizationAdapter>
                factory = new BreakUpCertainExecutorFactory<>();
        List<TransactionSynchronizationAdapter> parallelTransaction = factory.name("parallel transaction")
                .executor(this.executorService)
                .parallelNumber(8)
                .queueSize(10)
                .waitTime(10)
                .data(functions)
                .dataProcess(a -> a.apply(t))
                .withResult()
                .build()
                .active();
        parallelTransaction.forEach(TransactionSynchronizationManager::registerSynchronization);
    }

}
