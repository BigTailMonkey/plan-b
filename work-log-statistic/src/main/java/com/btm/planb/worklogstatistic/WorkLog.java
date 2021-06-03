package com.btm.planb.worklogstatistic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkLog {

    public List<WorkLogStandLine> analysis(String filePath) throws IOException {
        return analysis(new File(filePath));
    }

    public List<WorkLogStandLine> analysis(File file) throws IOException {
        List<WorkLogStandLine> standLines = new ArrayList<>();
        Analysister analysister = new Analysister();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader in = new BufferedReader(fileReader)) {
            String line;
            while ((line = in.readLine()) != null) {
                if (distinguishRealWorkLog(line)) {
                    standLines.add(analysister.statistic(line));
                }
            }
        }
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

}
