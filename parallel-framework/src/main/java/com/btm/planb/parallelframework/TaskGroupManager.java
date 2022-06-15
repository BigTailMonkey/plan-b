package com.btm.planb.parallelframework;

import com.sun.tools.javac.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    public <V> Carrier execute(String taskGroupName, Parameter parameter, V result) {
        Assert.check(taskGroups.containsKey(taskGroupName));
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
