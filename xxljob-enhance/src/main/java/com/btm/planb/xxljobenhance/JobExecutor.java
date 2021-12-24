package com.btm.planb.xxljobenhance;

import com.btm.planb.xxljobenhance.exceptions.JobExecuteFailException;
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

    private static String TICKET;
    private static final String rescheduleUrl = "http://{0}/xxl-job-admin/jobinfo/reschedule";
    private static final String triggerUrl = "http://{0}/xxl-job-admin/jobinfo/trigger";

    public JobExecutor(String ticket) {
        TICKET = ticket;
    }

    /**
     * 执行命令<br/>
     * 先调用xxl-job参数修改接口，修改后等待300ms
     * 在调用xxl-job执行接口
     *
     * @param host host地址
     * @param xxljobParam 定时任务的参数
     * @param executeParam 执行参数
     * @param suspendAfterFirstSuccess 执行完第一条之后是否暂停等待
     * @param suspendEveryOne 是否每执行一个行参数就等到确认
     */
    public void executePostRequestAll(String host, XxljobParam xxljobParam, List<String> executeParam, boolean suspendAfterFirstSuccess, boolean suspendEveryOne) {
        if (CollectionUtils.isEmpty(executeParam) || Objects.isNull(xxljobParam)) {
            throw new JobExecuteFailException("定时任务执行参数为空");
        }
        boolean checkUpdateParameter = true;
        try {
            for (int i = 0; i < executeParam.size(); i++) {
                Connection updateParamConnection = buildConnect(MessageFormat.format(rescheduleUrl, host), xxljobParam.updateExecutorParam(executeParam.get(i)));
                if (doExecuteWithPost(updateParamConnection)) {
                    // 修改参数后，间隔2秒再执行后续动作
                    Thread.sleep(2000);
                    if (checkUpdateParameter) {
                        System.out.println("请检查["+xxljobParam.getJobDesc()+"]的参数设置是否正确？(y/n)");
                        Scanner reader = new Scanner(System.in);
                        String c = reader.nextLine();
                        if (!"y".equalsIgnoreCase(c)) {
                            System.out.println("任务调用被手动中止。");
                            return;
                        } else {
                            checkUpdateParameter = false;
                        }
                    }
                    Map<String, String> dataMap = new HashMap<>();
                    dataMap.put("id",xxljobParam.getId());
                    Connection executeConnection = buildConnect(MessageFormat.format(triggerUrl, host), dataMap);
                    if (!doExecuteWithPost(executeConnection)) {
                        throw new JobExecuteFailException(executeConnection.toString());
                    }
                    System.out.println("执行成功，参数：" + executeParam.get(i));
                    if (suspendAfterFirstSuccess && i == 0) {
                        System.out.println("第"+(i + 1)+"条数据已执行成功，请验证后确定是否继续？(y/n)");
                        Scanner reader = new Scanner(System.in);
                        String s = reader.nextLine();
                        if (!"y".equalsIgnoreCase(s)) {
                            System.out.println("执行被手动终止");
                            break;
                        } else {
                            continue;
                        }
                    }
                    if (suspendEveryOne && i < executeParam.size() - 1) {
                        System.out.println("第"+(i + 1)+"条数据已执行成功，请验证后确定是否继续？(a/y/n)");
                        Scanner reader = new Scanner(System.in);
                        String s = reader.nextLine();
                        if ("a".equalsIgnoreCase(s)) {
                            suspendEveryOne = false;
                        } else if (!"y".equalsIgnoreCase(s)) {
                            System.out.println("执行被手动终止");
                            break;
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
        return ConnectionUtil.buildPostConnection(url, dataMap, TICKET);
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
        if (result.contains("\"code\":200,")) {
            return true;
        } else {
            throw new JobExecuteFailException(post.toString());
        }
    }
}
