package com.btm.planb.xxljobenhance;

import org.apache.commons.collections4.MapUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;

import java.util.Map;

public class ConnectionUtil {

    private ConnectionUtil() {}

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";

    /**
     * 构建一个post请求
     * @param url 请求地址
     * @param dataMap 请求参数
     * @param ticket 携带的cookie信息
     * @return 构造完成的连接对象
     */
    public static Connection buildPostConnection(String url, Map<String, String> dataMap, String ticket) {
        Connection connect = Jsoup.connect(url);
        connect.method(Connection.Method.POST);
        connect.header("content-type","application/x-www-form-urlencoded;charset=UTF-8");
        connect.userAgent(USER_AGENT);
        if (!StringUtil.isBlank(ticket)) {
            String[] cookies = ticket.split("=");
            connect.cookie(cookies[0], cookies[1]);
        }
        if (MapUtils.isNotEmpty(dataMap)) {
            connect.data(dataMap);
        }
        connect.ignoreContentType(true);
        return connect;
    }

    public static Connection buildLoginGetConnection(String url) {
        Connection connect = Jsoup.connect(url);
        connect.method(Connection.Method.GET);
        connect.userAgent(USER_AGENT);
        return connect;
    }
}
