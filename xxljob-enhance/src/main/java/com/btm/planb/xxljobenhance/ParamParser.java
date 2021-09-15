package com.btm.planb.xxljobenhance;

import com.btm.planb.xxljobenhance.model.XxljobParam;
import org.jsoup.internal.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ParamParser {

    private XxljobParam xxljobParam;
    private List<String> executeParam;
    private final String path;

    public ParamParser(String path) {
        if (StringUtil.isBlank(path)) {
            throw new RuntimeException("未指定参数文件地址");
        }
        this.path = path;
        parser();
    }

    public XxljobParam getXxljobParam() {
        return this.xxljobParam;
    }

    public List<String> getExecuteParam() {
        return this.executeParam;
    }

    private void parser() {
        try (FileReader fileReader = new FileReader(this.path);
             BufferedReader in = new BufferedReader(fileReader)) {
            this.xxljobParam = new XxljobParam();
            this.executeParam = new ArrayList<>();
            String line;
            while ((line = in.readLine()) != null) {
                if (line.trim().startsWith("#executorParam")) {
                    parserExecuteParam(in);
                    break;
                }
                if (StringUtil.isBlank(line) || line.trim().startsWith("#")) {
                    continue;
                }
                fullXxljobParam(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fullXxljobParam(String lineStr) {
        String[] strings = lineStr.trim().split("=");
        if (strings.length == 2) {
            String paramName = strings[0].trim();
            String paramValue = strings[1].trim();
            try {
                Method method = xxljobParam.getClass().getMethod("set" + convertInitialUpper(paramName), String.class);
                method.invoke(xxljobParam, paramValue);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void parserExecuteParam(BufferedReader reader) throws IOException{
        String line;
        while ((line = reader.readLine()) != null) {
            if (!StringUtil.isBlank(line)) {
                this.executeParam.add(line.trim());
            }
        }
    }

    private String convertInitialUpper(String words) {
        char[] chars = words.toCharArray();
        if (chars.length < 1) {
            throw new RuntimeException("非法的字符串");
        }
        // 首字母转大写(ASCII编码前移)
        chars[0] -= 32;
        return new String(chars);
    }
}
