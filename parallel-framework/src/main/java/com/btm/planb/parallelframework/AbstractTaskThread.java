package com.btm.planb.parallelframework;

import com.sun.tools.javac.util.Assert;

import java.util.Objects;


/**
 * 任务线程
 */
public abstract class AbstractTaskThread implements Runnable {

    protected final Carrier carrier;

    protected AbstractTaskThread(Carrier carrier) {
        this.carrier = carrier;
    }

    /**
     * 任务组名称<br/>
     * 子类可重写此方法为任务线程命名，任务线程管理器会根据
     * @return 任务组名称
     */
    public static String taskGroupName() {
        return "default";
    }

    /**
     * 任务线程的说明
     * @return
     */
    protected String taskDescription() {
        return null;
    }

    private String printTaskDescription() {
        return Objects.nonNull(taskDescription()) ? (taskDescription() + "-") : "";
    }

    @Override
    public void run() {
        Assert.checkNonNull(this.carrier);
        try {
            if (this.carrier.hasException()) {
                return;
            }
            this.doRun(this.carrier.getParameter());
        } catch (Throwable t) {
            this.carrier.setException(printTaskDescription() + t.getMessage());
        } finally {
            synchronized (carrier) {
                carrier.finishedOne();
            }
        }
    }

    public void doRun(Parameter parameter) {

    }
}
