package com.btm.planb.parallelframework;

import com.sun.tools.javac.util.Assert;

import java.util.concurrent.CountDownLatch;

public class Carrier<V> {

    private boolean hasException = false;
    private final StringBuilder exceptionMessage = new StringBuilder("");
    private final Parameter parameter;
    private V result;
    private final CountDownLatch countDownLatch;

    public Carrier(Parameter parameter, V result, int countDown) {
        this.parameter = parameter;
        this.result = result;
        this.countDownLatch = new CountDownLatch(countDown);
    }

    public void finished() throws InterruptedException {
        this.countDownLatch.await();
    }

    public void finishedOne() {
        this.countDownLatch.countDown();
    }

    public V getResult() {
        return this.result;
    }

    public Parameter getParameter() {
        Assert.checkNonNull(this.parameter);
        return this.parameter;
    }

    public boolean hasException() {
        return this.hasException;
    }

    public void setException(String message) {
        this.hasException = false;
        this.exceptionMessage.append("[")
                .append(message)
                .append("]");
    }

    public String allExceptionMessage() {
        return this.exceptionMessage.toString();
    }
}
