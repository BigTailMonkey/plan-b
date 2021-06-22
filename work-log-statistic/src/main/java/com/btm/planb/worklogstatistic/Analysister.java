package com.btm.planb.worklogstatistic;

import com.btm.planb.timeutil.DateFormat;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工作日志解析
 */
public class Analysister {

    private final String flagSign;
    private final String[] flagKeyWord;
    // 时间匹配 x月日，x月x号，xx-xx,yyyy-mm-dd，m.d日，m.d号
    private static final String DATETIME_FLAG_REGX = "[1-9]{1,2}月[01]{0,1}[0-9]{1,2}|[01]{0,1}[1-9]{1,2}-[01]{0,1}[0-9]{1,2}|\\d{4}-[01]{0,1}[1-9]{1,2}-[01]{0,1}[0-9]{1,2}|[1-9]{1,2}\\.[01]{0,1}[0-9]{1,2}";
    private static final String NUMBER_AS_INDEX = "^\\d{1,2}[\\.、]\\D";

    public Analysister() {
        this.flagSign = "[（\\(]\\S*[1-9]{1,2}月\\d+\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}-[01]{0,1}[1-9]{1,2}\\S*[\\)）]|[（\\(]\\S*[01]{0,1}[1-9]{1,2}\\.[01]{0,1}[1-9]{1,2}\\S*[\\)）]";
        this.flagKeyWord = new String[]{"调研", "方案设计", "开始开发", "开发中", "开发完成", "已完成", "提测", "上线", "已上线", "发布"};
    }

    public Analysister(String flagSign, String... flagKeyWord) {
        this.flagSign = flagSign;
        this.flagKeyWord = flagKeyWord;
    }

    /**
     * 解析一行工作日志
     *
     * @param oneLineLog 一行工作日志内容
     * @return 解析过的内容，即一个日志的标准行
     */
    public WorkLogStandLine statistic(String oneLineLog, String demandNumberRegex) {
        WorkLogStandLine standLine = new WorkLogStandLine(demandNumberRegex);
        // 记录原始内容
        standLine.setSourceStr(oneLineLog);
        // 去除多余的空格
        oneLineLog = oneLineLog.replace(" ", "");
        // 提取关键节点信息
        String flagSourceStr = findFlagStr(oneLineLog);
        if (Objects.nonNull(flagSourceStr)) {
            // 若能提取到关键节点信息则进行处理
            standLine = extractMainInfo(standLine, oneLineLog, flagSourceStr);
        } else {
            standLine.setWorkInfo(extractWorkInfo(oneLineLog));
            standLine.setErrorInfo("无关键时间节点信息");
        }
        return standLine;
    }

    /**
     * 查找节点信息
     *
     * @param oneLineLog 工作日志内容
     * @return 节点信息
     */
    public String findFlagStr(String oneLineLog) {
        oneLineLog = oneLineLog.replace(" ", "");
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
     * 解析工作日志的关键节点信息与工作内容信息
     *
     * @param standLine  工作日志标准行信息
     * @param oneLineLog 一行工作日志的内容
     * @param flagStr    关键节点信息
     * @return 工作日志标准行
     */
    public WorkLogStandLine extractMainInfo(WorkLogStandLine standLine, String oneLineLog, String flagStr) {
        // 找到关键节点信息，并将非关键节点信息之外的工作内容信息提取出来
        int flagIndex = oneLineLog.indexOf(flagStr);
        String workInfo = oneLineLog.substring(0, flagIndex);
        standLine.setWorkInfo(extractWorkInfo(workInfo));
        // 提取主要的关键节点信息原始数据，即将开头与结尾的括号去掉
        flagStr = flagStr.replaceAll("^[(（]|[)）]$", "");
        standLine.setFlagSourceInfo(flagStr);
        // 解析关键节点信息中的时间信息
        Pattern compile = Pattern.compile(DATETIME_FLAG_REGX);
        Matcher matcher = compile.matcher(flagStr);
        if (matcher.find()) {
            standLine.setDatetime(DateFormat.formatDateTime(matcher.group()));
            flagStr = flagStr.replaceAll(DATETIME_FLAG_REGX, "");
        }
        // 尝试匹配关键节点信息是否与已知的节点关键词已知且不含有其他内容，若能匹配则使用匹配内容
        for (String keyWord : flagKeyWord) {
            if (keyWord.equals(flagStr)) {
                standLine.setFlag(keyWord);
                return standLine;
            }
        }
        return standLine;
    }

    /**
     * 净化工作内容描述
     *
     * @param workInfo 工作内容描述
     * @return 净化后
     */
    public String extractWorkInfo(String workInfo) {
        // 将开头的编号信息去掉
        Pattern pattern = Pattern.compile(NUMBER_AS_INDEX);
        Matcher matcher = pattern.matcher(workInfo);
        if (matcher.find()) {
            return workInfo.replaceAll("^\\d{1,2}[\\.、]", "");
        }
        return workInfo;
    }
}
