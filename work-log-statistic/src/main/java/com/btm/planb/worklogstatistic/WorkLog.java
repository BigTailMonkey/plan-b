package com.btm.planb.worklogstatistic;

import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class WorkLog {

    public static final ThreadLocal<LogLineProperties> workLogProperties = new ThreadLocal<>();

    /**
     * 解析
     * @param filePath 文件路径
     * @return 工作日志标准行数据
     * @throws IOException
     */
    public List<WorkLogStandLine> analysis(String filePath) throws IOException, IllegalAccessException {
        return analysis(new File(filePath));
    }

    /**
     * 解析
     * @param file 文件
     * @return 工作日志标准行数据
     * @throws IOException
     */
    public List<WorkLogStandLine> analysis(File file) throws IOException, IllegalAccessException {
        List<WorkLogStandLine> standLines = new ArrayList<>();
        Analysister analysister = new Analysister();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader in = new BufferedReader(fileReader)) {
            LogLineProperties logLineProperties = loadWorkLogProperties();
            String line;
            String programGroup = "";
            while ((line = in.readLine()) != null) {
                if (logLineProperties.isProgramGroup(line)) {
                    // 项目集名称行
                    programGroup = line;
                } else if (distinguishRealWorkLog(line)) {
                    // 日志消息行
                    standLines.add(analysister.statistic(programGroup, line,"^[Bb][tT][Bb]-\\d{1,5}"));
                }
            }
        } finally {
            workLogProperties.remove();
        }
        signDiffOrDeleteRepeat(standLines);
        Collections.sort(standLines);
        return standLines;
    }

    /**
     * 加载配置文件
     * @throws IOException 文件读取异常
     */
    public LogLineProperties loadWorkLogProperties() throws IOException, IllegalAccessException {
        Properties properties = new Properties();
        InputStream in = WorkLog.class.getClassLoader().getResourceAsStream("WorkLog.properties");
        if (Objects.nonNull(in)) {
            properties.load(new InputStreamReader(in));
            workLogProperties.set(readProperties(properties));
            return workLogProperties.get();
        }
        return new LogLineProperties();
    }

    /**
     * 读取配置文件，构建日志行配置信息
     * @param properties 配置信息
     * @return 日志行配置信息
     */
    public LogLineProperties readProperties(Properties properties) throws IllegalAccessException {
        LogLineProperties logLineProperties = new LogLineProperties();
        Field[] declaredFields = LogLineProperties.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            PropertiesName annotation = field.getAnnotation(PropertiesName.class);
            if (Objects.nonNull(annotation)) {
                String property = properties.getProperty(annotation.value());
                if (Objects.nonNull(property) && property.length() > 0) {
                    String[] strings = StringUtils.splitString(property, ",");
                    field.set(logLineProperties,strings);
                }
            }
        }
        return logLineProperties;
    }

    /**
     * 辨别是否为一行有效的内容
     * @param oneLineLog 一行工作日志内容
     * @return 是否是一行真实的的工作日志
     */
    public boolean distinguishRealWorkLog(String oneLineLog) {
        oneLineLog = oneLineLog.replaceAll("[\\pP\\pS\\pZ ]", "");
        return !"".equals(oneLineLog) && !"本周".equals(oneLineLog) && !"下周".equals(oneLineLog)
                && !"问题".equals(oneLineLog) && !"上线".equals(oneLineLog);
    }

    /**
     * 过滤是否有重复
     * @param standLines 标准日志文档
     */
    public void signDiffOrDeleteRepeat(List<WorkLogStandLine> standLines) {
        Set<String> singleLine = new HashSet<>();
        for (WorkLogStandLine standLine : standLines) {
            String repeatFlag = standLine.getProgramName() + standLine.getDemandNumber();
                if (singleLine.contains(repeatFlag)) {
                    standLine.setErrorInfo("疑似重复");
                } else {
                    singleLine.add(repeatFlag);
                }
        }
    }

}
