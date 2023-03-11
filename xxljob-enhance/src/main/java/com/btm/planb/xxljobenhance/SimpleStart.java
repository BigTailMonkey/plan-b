package com.btm.planb.xxljobenhance;

import com.btm.planb.xxljobenhance.model.XxljobParam;

import java.util.Objects;

public class SimpleStart {

    public void run(String paramFilePath) {
        String ticket = null;
        ParamParser paramParser = new ParamParser(paramFilePath);
        XxljobParam param = paramParser.getXxljobParam();
        String host = param.getHost();
        System.out.println("参数解析完成，定时任务执行器地址:[" + host + "]，执行登陆操作…………");
        if (param.isNeedTicket()) {
            // 执行登陆动作，拿到登陆信息
            LoginProxy<String> loginProxy = new LoginProxy<String>() {
                @Override
                public String getLoginTicket(String host, XxljobParam param) {
                    // 实现LoginProxy接口，编写登陆令牌获取逻辑代码，替换此部分后再打包使用
                    throw new NullPointerException("请编辑登陆令牌获取逻辑");
                }
            };
            ticket = loginProxy.getLoginTicket(host, param);
            if (Objects.isNull(ticket)) {
                System.out.println("登陆失败，请检查登陆信息是否正确、有效");
                System.exit(0);
            }
            System.out.println("登陆验证通过…………获得登陆令牌信息：" + ticket);
        } else {
            System.out.println("无需登陆验证，继续执行");
        }
        // 根据关键信息查询定时任务，并与其他信息进行比较，确保调用正确的定时任务
        JobListReader jobListReader = new JobListReader(ticket);
        if (jobListReader.checkJobInfo(host, param)) {
            System.out.println("["+param.getJobDesc()+"]信息检查通过，开始执行批量调用");
            JobExecutor jobExecutor = new JobExecutor(ticket, host);
            // 使用参数执行调用
            jobExecutor.executePostRequestAll(paramParser.getXxljobParam(), paramParser.getExecuteParam(), true, true);
        } else {
            System.out.println("未查询到对应的定时任务job对象，请核对信息后重试");
        }
    }

}
