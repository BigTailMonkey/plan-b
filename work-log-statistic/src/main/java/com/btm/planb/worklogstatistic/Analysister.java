package com.btm.planb.worklogstatistic;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工作日志解析
 */
public class Analysister {

    private final String flagSign;
    private final String[] flagKeyWord;
    // 时间匹配 x月日，x月x号，xx-xx,yyyy-mm-dd
    private static final String DATETIME_FLAG_REGX = "[1-9]{1,2}月[01]{0,1}[1-9]{1,2}[日号]|[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}|\\d{4}-[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}";
    private static final String NUMBER_AS_INDEX = "^\\d{1,2}[\\.、]\\D";

    public Analysister() {
        this.flagSign = "[（\\(]\\S*[1-9]{1,2}月\\d+[日号]\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        this.flagKeyWord = new String[]{"设计","提测","上线","发布"};
    }

    public Analysister(String flagSign, String... flagKeyWord) {
        this.flagSign = flagSign;
        this.flagKeyWord = flagKeyWord;
    }

    /**
     * 解析一行工作日志
     * @param oneLineLog 一行工作日志内容
     * @return 解析过的内容，即一个日志的标准行
     */
    public WorkLogStandLine statistic(String oneLineLog) {
        WorkLogStandLine standLine = new WorkLogStandLine();
        standLine.setSourceStr(oneLineLog);
        oneLineLog = oneLineLog.replace(" ","");
        String flagStr = findFlagStr(oneLineLog);
        if (Objects.nonNull(flagStr)) {
            standLine = extractMainInfo(standLine, oneLineLog, flagStr);
        } else {
            standLine.setErrorInfo("无关键时间节点信息");
        }
        return standLine;
    }

    /**
     * 查找标识位置
     * @param oneLineLog 工作日志内容
     * @return 起始的位置下标
     */
    public String findFlagStr(String oneLineLog) {
        oneLineLog = oneLineLog.replace(" ","");
        Pattern pattern = Pattern.compile(flagSign);
        Matcher matcher = pattern.matcher(oneLineLog);
        while (matcher.find()) {
            String flagStr = matcher.group();
            for (String keyWord : flagKeyWord) {
                if (flagStr.contains(keyWord)) {
                    return flagStr;
                }
            }
        }
        return null;
    }

    /**
     * 解析工作日志的关键信息
     * @param standLine 工作日志标准行信息
     * @param oneLineLog 一行工作日志的内容
     * @param flagStr 关键时间节点信息
     * @return 工作日志标准行
     */
    public WorkLogStandLine extractMainInfo(WorkLogStandLine standLine, String oneLineLog, String flagStr) {
        int flagIndex = oneLineLog.indexOf(flagStr);
        String workInfo = oneLineLog.substring(0,flagIndex);
        standLine.setWorkInfo(extractWorkInfo(workInfo));
        Pattern compile = Pattern.compile(DATETIME_FLAG_REGX);
        Matcher matcher = compile.matcher(flagStr);
        if (matcher.find()) {
            standLine.setDatetime(matcher.group());
        }
        for (String keyWord : flagKeyWord) {
            if (flagStr.contains(standLine.getDatetime() + keyWord)) {
                standLine.setFlag(keyWord);
                break;
            }
        }
        return standLine;
    }

    /**
     * 净化工作内容描述
     * @param workInfo 工作内容描述
     * @return 净化后
     */
    public String extractWorkInfo(String workInfo) {
        Pattern pattern = Pattern.compile(NUMBER_AS_INDEX);
        Matcher matcher = pattern.matcher(workInfo);
        if (matcher.find()) {
            return workInfo.replace(matcher.group(),"");
        }
        return workInfo;
    }
}
