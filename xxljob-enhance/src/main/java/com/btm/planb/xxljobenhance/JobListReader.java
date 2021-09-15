package com.btm.planb.xxljobenhance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.btm.planb.xxljobenhance.exceptions.JobExecuteFailException;
import com.btm.planb.xxljobenhance.model.JobInfo;
import com.btm.planb.xxljobenhance.model.XxljobParam;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JobListReader {

    private final String ticket;
    private static final String pageListUrl = "http://{0}/xxl-job-admin/jobinfo/pageList";

    public JobListReader(String ticket) {
        this.ticket =ticket;
    }

    public String getTicket() {
        return this.ticket;
    }

    public boolean checkJobInfo(String host, XxljobParam param) {
        JobInfo jobInfo = parseJobPageListElement(host, param.getJobGroupId(), param.getExecutorHandler());
        if (Objects.isNull(jobInfo)) {
            return false;
        }
        return param.getId().equals(String.valueOf(jobInfo.getId()))
                && param.getJobDesc().equals(jobInfo.getJobDesc())
                && param.getAuthor().equals(jobInfo.getAuthor());
    }

    public JobInfo parseJobPageListElement(String host, String jobGroupId, String executorHandler) {
        try {
            Document post = reader(host, jobGroupId, executorHandler);
            if (200 == post.connection().response().statusCode()) {
                Object data = JSON.parseObject(post.body().text()).get("data");
                JSONArray jsonObject = JSON.parseObject(String.valueOf(data), JSONArray.class);
                return jsonObject.getObject(0, JobInfo.class);
            } else {
                System.out.println(post);
                throw new JobExecuteFailException("接口响应码错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document reader(String host, String jobGroupId, String executorHandler) throws IOException {
        String url = MessageFormat.format(pageListUrl, host);
        Connection connection = ConnectionUtil.buildPostConnection(url, buildData(jobGroupId, executorHandler), ticket);
        return connection.post();
    }

    private Map<String, String> buildData(String jobGroupId, String executorHandler) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("jobGroup", jobGroupId);
        dataMap.put("jobDesc", "");
        dataMap.put("executorHandler", executorHandler);
        dataMap.put("start", "0");
        dataMap.put("length", "10");
        return dataMap;
    }
}
