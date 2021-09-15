package com.btm.planb.xxljobenhance;

import com.btm.planb.xxljobenhance.model.LoginInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;

import java.util.Map;

public class ConnectionUtil {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";

    public static Connection buildPostConnection(String url, Map<String, String> dataMap, String ticket) {
        Connection connect = Jsoup.connect(url);
        connect.method(Connection.Method.POST);
        connect.header("content-type","application/x-www-form-urlencoded;charset=UTF-8");
        connect.userAgent(USER_AGENT);
        if (!StringUtil.isBlank(ticket)) {
            connect.cookie("SESSION", ticket);
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
