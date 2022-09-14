package com.btm.planb.parallel.thread.transaction;

public abstract class TransactionThread implements Runnable {

    public abstract void doThing();

    @Override
    public void run() {
        try {
            doThing();
        } catch (Exception e) {
            // todo 设置为需要回滚
        }
    }
}
