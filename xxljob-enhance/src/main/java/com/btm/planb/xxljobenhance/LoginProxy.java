package com.btm.planb.xxljobenhance;

import com.btm.planb.xxljobenhance.model.XxljobParam;


public interface LoginProxy<T> {

    /**
     * 获得登陆标识，使用者需要根据自己系统的情况实现此接口方法
     * @param host 登陆接口地址
     * @param param 参数配置
     * @return 登陆标识
     */
    T getLoginTicket(String host, XxljobParam param);

}
