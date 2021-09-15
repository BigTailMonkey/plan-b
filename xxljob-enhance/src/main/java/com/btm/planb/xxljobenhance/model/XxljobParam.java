package com.btm.planb.xxljobenhance.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * xxl-job单个任务的所有参数，
 */
public class XxljobParam {

    private Map<String, String> paramMap;

    private String host;

    private String userName;
    private String password;
    private String needTicket;

    private String jobDesc = "";
    private String executorRouteStrategy = "";
    private String jobCron = "";
    private String executorHandler = "";
    private String executorParam = "";
    private String childJobId = "";
    private String executorBlockStrategy = "";
    private String executorFailStrategy = "";
    private String author = "";
    private String alarmEmail = "";
    private String alarmDingDing = "";
    private String registerAddress = "";
    private String id = "";
    private String jobGroupId = "";

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        if (Objects.isNull(host)) {
            throw new RuntimeException("host 不能为空");
        }
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNeedTicket() {
        return "true".equals(needTicket);
    }

    public void setNeedTicket(String needTicket) {
        this.needTicket = needTicket;
    }

    public String getJobDesc() {
        if (Objects.isNull(jobDesc)) {
            throw new RuntimeException("jobDesc 不能为空");
        }
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getExecutorRouteStrategy() {
        if (Objects.isNull(executorRouteStrategy)) {
            throw new RuntimeException("executorRouteStrategy 不能为空");
        }
        return executorRouteStrategy;
    }

    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
    }

    public String getJobCron() {
        if (Objects.isNull(jobCron)) {
            throw new RuntimeException("jobCron 不能为空");
        }
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public String getExecutorHandler() {
        if (Objects.isNull(executorHandler)) {
            throw new RuntimeException("executorHandler 不能为空");
        }
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        if (Objects.isNull(executorParam)) {
            throw new RuntimeException("executorParam 不能为空");
        }
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getChildJobId() {
        return childJobId;
    }

    public void setChildJobId(String childJobId) {
        this.childJobId = childJobId;
    }

    public String getExecutorBlockStrategy() {
        if (Objects.isNull(executorBlockStrategy)) {
            throw new RuntimeException("executorBlockStrategy 不能为空");
        }
        return executorBlockStrategy;
    }

    public void setExecutorBlockStrategy(String executorBlockStrategy) {
        this.executorBlockStrategy = executorBlockStrategy;
    }

    public String getExecutorFailStrategy() {
        if (Objects.isNull(executorFailStrategy)) {
            throw new RuntimeException("executorFailStrategy 不能为空");
        }
        return executorFailStrategy;
    }

    public void setExecutorFailStrategy(String executorFailStrategy) {
        this.executorFailStrategy = executorFailStrategy;
    }

    public String getAuthor() {
        if (Objects.isNull(author)) {
            throw new RuntimeException("author 不能为空");
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public String getAlarmDingDing() {
        return alarmDingDing;
    }

    public void setAlarmDingDing(String alarmDingDing) {
        this.alarmDingDing = alarmDingDing;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getId() {
        if (Objects.isNull(id)) {
            throw new RuntimeException("id 不能为空");
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobGroupId() {
        if (Objects.isNull(jobGroupId)) {
            throw new RuntimeException("jobGroupId 不能为空");
        }
        return jobGroupId;
    }

    public void setJobGroupId(String jobGroupId) {
        this.jobGroupId = jobGroupId;
    }

    public Map<String, String> getAllParam() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("jobDesc", getJobDesc());
        dataMap.put("executorRouteStrategy", getExecutorRouteStrategy());
        dataMap.put("jobCron", getJobCron());
        dataMap.put("executorHandler", getExecutorHandler());
        dataMap.put("executorParam", getExecutorParam());
        dataMap.put("childJobId", getChildJobId());
        dataMap.put("executorBlockStrategy", getExecutorBlockStrategy());
        dataMap.put("executorFailStrategy", getExecutorFailStrategy());
        dataMap.put("author", getAuthor());
        dataMap.put("alarmEmail", getAlarmEmail());
        dataMap.put("alarmDingDing", getAlarmDingDing());
        dataMap.put("registerAddress", getRegisterAddress());
        dataMap.put("id", getId());
        return dataMap;
    }

    public void initDataMap() {
        this.paramMap = getAllParam();
    }

    public Map<String, String> updateExecutorParam(String executorParam) {
        if (Objects.isNull(this.paramMap)) {
            initDataMap();
        }
        this.paramMap.put("executorParam", executorParam);
        return this.paramMap;
    }
}
