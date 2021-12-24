package com.btm.planb.xxljobenhance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.btm.planb.xxljobenhance.exceptions.JobExecuteFailException;
import com.btm.planb.xxljobenhance.model.JobInfo;
import com.btm.planb.xxljobenhance.model.XxljobParam;
import org.jsoup.Connection;
import org.jsoup.internal.StringUtil;
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

    /**
     * 校验查询到的xxl-job任务信息与输入的信息是否一致，校验项有：
     * <li>id-任务ID</li>
     * <li>jobDoc-任务描述</li>
     * <li>author-责任人</li>
     * @param host xxl-job服务地址
     * @param param 配置文件
     * @return
     */
    public boolean checkJobInfo(String host, XxljobParam param) {
        JobInfo jobInfo = parseJobPageListElement(host, param.getJobGroupId(), param.getExecutorHandler());
        if (Objects.isNull(jobInfo)) {
            return false;
        }
        if (param.getId().equals(String.valueOf(jobInfo.getId()))
                && param.getJobDesc().equals(jobInfo.getJobDesc())
                && param.getAuthor().equals(jobInfo.getAuthor())) {
            if (StringUtil.isBlank(param.getAlarmDingDing())) {
                param.setAlarmDingDing(jobInfo.getAlarmDingDing());
            }
            if (StringUtil.isBlank(param.getAlarmEmail())) {
                param.setAlarmDingDing(jobInfo.getAlarmEmail());
            }
            if (StringUtil.isBlank(param.getChildJobId())) {
                param.setChildJobId(jobInfo.getChildJobId());
            }
            if (StringUtil.isBlank(param.getExecutorBlockStrategy())) {
                param.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
            }
            if (StringUtil.isBlank(param.getExecutorFailStrategy())) {
                param.setExecutorFailStrategy(jobInfo.getExecutorFailStrategy());
            }
            if (StringUtil.isBlank(param.getExecutorRouteStrategy())) {
                param.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
            }
            if (StringUtil.isBlank(param.getJobCron())) {
                param.setJobCron(jobInfo.getJobCron());
            }
            if (StringUtil.isBlank(param.getRegisterAddress())) {
                param.setRegisterAddress(jobInfo.getRegisterAddress());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据执行器ID和定时任务的名称（jobHandler）查询定时任务信息
     * @param host xxl-job服务地址
     * @param jobGroupId 执行期ID
     * @param executorHandler jobHandler，定时任务的名称
     * @return
     */
    public JobInfo parseJobPageListElement(String host, String jobGroupId, String executorHandler) {
        try {
            Document post = reader(host, jobGroupId, executorHandler);
            if (200 == post.connection().response().statusCode()) {
                Object data = JSON.parseObject(post.body().text()).get("data");
                JSONArray jsonArray = JSON.parseObject(String.valueOf(data), JSONArray.class);
                if (Objects.isNull(jsonArray) || jsonArray.isEmpty()) {
                    throw new JobExecuteFailException("未查询到定时任务执行器");
                }
                return jsonArray.getObject(0, JobInfo.class);
            } else {
                System.out.println(post);
                throw new JobExecuteFailException("接口响应码错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询定时任务信息
     * @param host xxl-job服务地址
     * @param jobGroupId 执行器ID
     * @param executorHandler 定时任务名称
     * @return
     * @throws IOException
     */
    private Document reader(String host, String jobGroupId, String executorHandler) throws IOException {
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
