package com.btm.planb.parallel.thread.transaction;

import com.btm.planb.parallel.framework.BreakUpCertainExecutorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 依赖于Spring的多线程公用事务的线程管理器。<br/>
 * <p>设计思想：将复杂逻辑交由子线程并发执行，最终需要事务操作数据库时，使用{@link TransactionSynchronizationAdapter}将SQL脚本注册回请求处理主线程，
 * 由请求处理主线程统一进行提交</p>
 *
 * @param <T> 并行线程执行方法的入参
 */
public class SpringTransactionThreadManager<T> {

    private final Logger logger = LoggerFactory.getLogger(SpringTransactionThreadManager.class);

    private final ExecutorService executorService;

    private final List<Function<T, TransactionSynchronizationAdapter>> functions = new ArrayList<>();

    public SpringTransactionThreadManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void add(Function<T, TransactionSynchronizationAdapter> function) {
        this.functions.add(function);
    }

    public <Z> void addBeforeCommit(Function<T, Z> subThreadLogic, Consumer<Z> transactionSql) {
        Function<T, TransactionSynchronizationAdapter> function = a -> {
            final Z z = subThreadLogic.apply(a);
            return new TransactionSynchronizationAdapter() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    if (readOnly) {
                        throw new TransactionReadOnlyException("transaction is readonly");
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("add new sub transaction thread.");
                    }
                    transactionSql.accept(z);
                }
            };
        };
        this.functions.add(function);
    }

    public void addAction(T t) throws ExecutionException {
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
