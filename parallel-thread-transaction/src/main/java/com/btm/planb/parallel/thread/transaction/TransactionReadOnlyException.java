package com.btm.planb.parallel.thread.transaction;

/**
 * 事务只读异常，当发现事务为只读事务时，则不允许执行commit动作
 */
public class TransactionReadOnlyException extends RuntimeException {

    public TransactionReadOnlyException(String message) {
        super(message);
    }

    public TransactionReadOnlyException(String message, Exception exception) {
        super(message, exception);
    }

}
