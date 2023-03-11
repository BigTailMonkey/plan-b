package com.btm.planb.xxljobenhance;

import com.btm.planb.xxljobenhance.exceptions.JobExecuteFailException;
import com.btm.planb.xxljobenhance.model.JobInfo;
import com.btm.planb.xxljobenhance.model.XxljobParam;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 定时任务执行器
 */
public class JobExecutor {

    private static String ticket;
    private static String hostUrl;
    private static final int MAX_TRY = 5;
    private static final String RESCHEDULE_URL = "http://{0}/xxl-job-admin/jobinfo/reschedule";
    private static final String TRIGGER_URL = "http://{0}/xxl-job-admin/jobinfo/trigger";

    public JobExecutor(String ticket, String host) {
        JobExecutor.ticket = ticket;
        hostUrl = host;
    }

    /**
     * 执行命令<br/>
     * 先调用xxl-job参数修改接口，修改后等待300ms
     * 在调用xxl-job执行接口
     *
     * @param xxljobParam 定时任务的参数
     * @param paramList 执行参数列表
     * @param suspendAfterFirstSuccess 执行完第一条之后是否暂停等待
     * @param suspendEveryOne 是否每执行一个行参数就等到确认
     */
    public void executePostRequestAll(XxljobParam xxljobParam, List<String> paramList, boolean suspendAfterFirstSuccess, boolean suspendEveryOne) {
        if (CollectionUtils.isEmpty(paramList) || Objects.isNull(xxljobParam)) {
            throw new JobExecuteFailException("定时任务执行参数为空");
        }
        // 单个参数参数执行的数据计数
        int tryCount = 0;
        boolean waitConfirmation = true;
        try {
            for (int i = 0; i < paramList.size(); i++) {
                String param = paramList.get(i);
                if (doExecuteWithPost(updateParameter(xxljobParam, param))) {
                    // 查询刚刚的参数修改是否生效
                    if (!checkParam(xxljobParam, param)) {
                        if (tryCount >= MAX_TRY) {
                            throw new JobExecuteFailException("参数[" + param +"]设置失败超过最大重试次数：" + MAX_TRY + "次");
                        }
                        tryCount ++;
                        i --;
                        // 参数修改失败，等待一秒之后再试
                        Thread.sleep(1000);
                        continue;
                    }
                    if (tryCount > 0) {
                        System.out.println("参数[" + param + "]设置重试：" + tryCount + "次");
                    }
                    tryCount = 0;

                    if (waitConfirmation) {
                        if (confirm(xxljobParam)) {
                            waitConfirmation = false;
                        } else {
                            return;
                        }
                    }

                    // 执行任务
                    runJob(xxljobParam, param);

                    if (suspendAfterFirstSuccess && i == 0) {
                        System.out.println("第"+(i + 1)+"条数据已执行成功，请验证后确定是否继续？(y/n)");
                        Scanner reader = new Scanner(System.in);
                        String s = reader.nextLine();
                        if (!"y".equalsIgnoreCase(s)) {
                            System.out.println("执行被手动终止");
                            // 直接结束方法的执行
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (suspendEveryOne && i < paramList.size() - 1) {
                        System.out.println("第"+(i + 1)+"条数据已执行成功，请验证后确定是否继续(a/y)或重新执行当前数据(r)?");
                        Scanner reader = new Scanner(System.in);
                        String s = reader.nextLine();
                        if ("a".equalsIgnoreCase(s)) {
                            suspendEveryOne = false;
                        } else if ("r".equalsIgnoreCase(s)) {
                            i --;
                            continue;
                        } else if (!"y".equalsIgnoreCase(s)) {
                            System.out.println("执行被手动终止");
                            return;
                        }
                    }
                }
            }
            System.out.println("全部参数已全部执行完成");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建连接
     * @param url 请求地址
     * @param dataMap 请求参数，key：参数名；value：参数值
     * @return 连接
     */
    private Connection buildConnect(String url, Map<String, String> dataMap) {
        return ConnectionUtil.buildPostConnection(url, dataMap, ticket);
    }

    /**
     * 以POST请求方式执行
     * @param connection 连接
     * @return true：执行成功
     * @throws IOException
     */
    private boolean doExecuteWithPost(Connection connection) throws IOException {
        Document post = connection.post();
        String result = post.getElementsByTag("body").text();
        return result.contains("\"code\":200,");
    }

    /**
     * @return true：确认继续执行；false：终止执行
     */
    private boolean confirm(XxljobParam xxljobParam) {
        System.out.println("请检查["+xxljobParam.getJobDesc()+"]的参数设置是否正确？(y/n)");
        Scanner reader = new Scanner(System.in);
        String c = reader.nextLine();
        if (!"y".equalsIgnoreCase(c)) {
            System.out.println("任务调用被手动中止。");
            // 直接结束方法的执行
            return false;
        } else {
            return true;
        }
    }

    private Connection updateParameter(XxljobParam xxljobParam, String executeParam) {
        return buildConnect(MessageFormat.format(RESCHEDULE_URL, hostUrl), xxljobParam.updateExecutorParam(executeParam));
    }

    /**
     * @return true：检查通过；false-检查未通过
     */
    private boolean checkParam(XxljobParam xxljobParam, String executeParam) {
        JobListReader reader = new JobListReader(ticket);
        JobInfo jobInfo = reader.parseJobPageListElement(hostUrl, xxljobParam.getJobGroupId(),
            xxljobParam.getExecutorHandler());
        return Objects.nonNull(jobInfo) && executeParam.equals(jobInfo.getExecutorParam());
    }

    private void runJob(XxljobParam xxljobParam, String executeParam) throws IOException {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("id",xxljobParam.getId());
        Connection executeConnection = buildConnect(MessageFormat.format(TRIGGER_URL, hostUrl), dataMap);
        if (!doExecuteWithPost(executeConnection)) {
            throw new JobExecuteFailException(executeConnection.toString());
        }
        System.out.println("执行成功，参数：" + executeParam);
    }
}
