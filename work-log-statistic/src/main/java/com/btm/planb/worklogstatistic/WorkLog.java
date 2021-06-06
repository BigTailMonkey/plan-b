package com.btm.planb.worklogstatistic;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WorkLog {

    /**
     * 解析
     * @param filePath 文件路径
     * @return 工作日志标准行数据
     * @throws IOException
     */
    public List<WorkLogStandLine> analysis(String filePath) throws IOException {
        return analysis(new File(filePath));
    }

    /**
     * 解析
     * @param file 文件
     * @return 工作日志标准行数据
     * @throws IOException
     */
    public List<WorkLogStandLine> analysis(File file) throws IOException {
        List<WorkLogStandLine> standLines = new ArrayList<>();
        Analysister analysister = new Analysister();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader in = new BufferedReader(fileReader)) {
            String line;
            while ((line = in.readLine()) != null) {
                if (distinguishRealWorkLog(line)) {
                    standLines.add(analysister.statistic(line,"^[Bb][tT][Bb]-\\d{1,5}"));
                }
            }
        }
        signDiffOrDeleteRepeat(standLines);
        Collections.sort(standLines);
        return standLines;
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
     * @param standLines
     */
    public void signDiffOrDeleteRepeat(List<WorkLogStandLine> standLines) {
        Set<String> singleLine = new HashSet<>();
        for (WorkLogStandLine standLine : standLines) {
            if (Objects.nonNull(standLine.getDemandNumber()) && !"".equals(standLine.getDemandNumber())) {
                if (singleLine.contains(standLine.getDemandNumber())) {
                    standLine.setErrorInfo("疑似重复");
                } else {
                    singleLine.add(standLine.getDemandNumber());
                }
            }
        }
    }

}
