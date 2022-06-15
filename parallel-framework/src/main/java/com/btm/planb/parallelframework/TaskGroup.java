package com.btm.planb.parallelframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 任务组
 */
public class TaskGroup {

    public final Actuator actuator;

    public TaskGroup(Actuator actuator) {
        this.actuator = actuator;
    }

    public final List<Class<AbstractTaskThread>> taskThreads = new CopyOnWriteArrayList<>();

    public void addTask(Class<AbstractTaskThread> taskThreadClass) {
        this.taskThreads.add(taskThreadClass);
    }

    public int taskNumber() {
        return taskThreads.size();
    }

    /**
     * 执行当前任务组下的全部任务
     * @param carrier
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public void executeAll(Carrier carrier) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
        for (Class<AbstractTaskThread> taskThread : taskThreads) {
            Constructor<AbstractTaskThread> constructor = taskThread.getConstructor(Carrier.class);
            AbstractTaskThread abstractTaskThread = constructor.newInstance(carrier);
            this.actuator.execute(abstractTaskThread);
        }
        carrier.finished();
    }
}
