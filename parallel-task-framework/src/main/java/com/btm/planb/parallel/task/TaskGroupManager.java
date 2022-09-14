package com.btm.planb.parallel.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程组管理器，
 */
public class TaskGroupManager {

    private static final List<Class<AbstractTaskThread>> ALL_TASK_THREAD = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, TaskGroup> taskGroups = new ConcurrentHashMap<>();

    public void add(Class<AbstractTaskThread> taskThreadClass) {
        ALL_TASK_THREAD.add(taskThreadClass);
    }

    /**
     * 按照任务线程所属组的名称，进行分组
     * @param actuator
     */
    public void group(Actuator actuator) {
        try {
            for (Class<AbstractTaskThread> taskThreadClass : ALL_TASK_THREAD) {
                Method taskGroupNameMethod = taskThreadClass.getMethod("taskGroupName");
                String taskGroupName = (String)taskGroupNameMethod.invoke(null);
                if (!taskGroups.containsKey(taskGroupName)) {
                    taskGroups.put(taskGroupName, new TaskGroup(actuator));
                }
                taskGroups.get(taskGroupName).addTask(taskThreadClass);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用指定的一组任务线程组，对传入的执行参数进行并发处理。
     * @param taskGroupName 任务线程组
     * @param parameter 执行参数
     * @param result 执行结果
     * @param <V> 执行结果的类型
     * @return
     */
    public <V> Carrier execute(String taskGroupName, Parameter parameter, V result) {
        if (!taskGroups.containsKey(taskGroupName)) {
            throw new RuntimeException("未找到任务组：" + taskGroupName);
        }
        TaskGroup taskGroup = taskGroups.get(taskGroupName);
        Carrier carrier = new Carrier(parameter, result, taskGroup.taskNumber());
        try {
            taskGroup.executeAll(carrier);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return carrier;
    }

}
