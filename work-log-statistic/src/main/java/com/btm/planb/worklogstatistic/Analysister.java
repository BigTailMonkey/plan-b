package com.btm.planb.worklogstatistic;

import com.btm.planb.timeutil.Constant;
import com.btm.planb.timeutil.DateExtractor;
import com.btm.planb.worklogstatistic.keyNode.INode;
import com.btm.planb.worklogstatistic.keyNode.NodeChain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工作日志解析
 */
public class Analysister {

    private final String flagSign;
    // 时间匹配
    private static final String DATETIME_FLAG_REGX = Constant.DATE_FORMAT;
    // 文字开头编号
    private static final String NUMBER_AS_INDEX = "^\\d{1,2}[\\.、]\\D";
    // 文字开头与结尾的括号
    private static final String BRACKETS = "^[(（]|[)）]$";

    private final DateExtractor dateExtractor = new DateExtractor();

    public Analysister() {
        this.flagSign = "[\\(|（][^\\)^）]*"+ Constant.DATE_FORMAT +"[^\\(^（]*[\\)|）]";
    }

    public Analysister(String flagSign) {
        this.flagSign = flagSign;
    }

    /**
     * 解析一行工作日志
     *
     * @param programGroup 所属项目集
     * @param oneLineLog 一行工作日志内容
     * @param demandNumberRegex 需求编号匹配正则表达式
     * @return 解析过的内容，即一个日志的标准行
     */
    public WorkLogStandLine statistic(String programGroup, String oneLineLog, String demandNumberRegex) {
        WorkLogStandLine standLine = new WorkLogStandLine(demandNumberRegex);
        // 记录原始内容
        standLine.setSourceStr(oneLineLog);
        // 去除多余的空格
        oneLineLog = oneLineLog.replace(" ", "");
        // 若能提取到关键节点信息则进行处理
        standLine = extractMainInfo(standLine, oneLineLog);
        // 设置所属项目集
        standLine = setProgramName(standLine, programGroup);
        // 判定是否为重点项目
        standLine.setMajorProgram(WorkLog.workLogProperties.get().isMainProgram(standLine.getWorkInfo()));
        return standLine;
    }

    /**
     * 设置项目集名称
     * @param standLine 标准工作日志行
     * @param defaultProgramName 默认项目集名称
     * @return 标准日志行
     */
    public WorkLogStandLine setProgramName(WorkLogStandLine standLine, String defaultProgramName) {
        standLine.setProgramName(defaultProgramName);
        String workInfo = standLine.getWorkInfo();
        Matcher matcher = Pattern.compile("^[\\[【].+?[】\\]]").matcher(workInfo);
        if (matcher.find()) {
            String programName = matcher.group();
            programName = programName.substring(1, Math.max(programName.length() - 1, 1));
            System.out.println(programName);
            if (WorkLog.workLogProperties.get().isMainProgram(programName)) {
                standLine.setProgramName(programName);
            }
        }
        return standLine;
    }

    /**
     * 解析工作日志的关键节点信息与工作内容信息
     *
     * @param standLine  工作日志标准行信息
     * @param oneLineLog 一行工作日志的内容
     * @return 工作日志标准行
     */
    public WorkLogStandLine extractMainInfo(WorkLogStandLine standLine, String oneLineLog) {
        NodeChain nodeChain = new NodeChain();
        Pattern pattern = Pattern.compile(flagSign);
        Matcher matcher = pattern.matcher(oneLineLog);
        String flagStr = "";
        while (matcher.find()) {
            flagStr = matcher.group();
        }
        if (!"".equals(flagStr)) {
            // 提取主要的关键节点信息原始数据，即将开头与结尾的括号去掉
            String mainDate = flagStr.replaceAll(BRACKETS, "");
            standLine.setFlagSourceInfo(mainDate);
            // 解析关键节点信息中的时间信息
            Pattern compile = Pattern.compile(DATETIME_FLAG_REGX);
            Matcher dateMatcher = compile.matcher(mainDate);
            if (dateMatcher.find()) {
                String dateStr = dateMatcher.group();
                standLine.setDatetime(dateExtractor.extractLast(dateStr));
                INode keyNode = nodeChain.findNode(flagStr.substring(flagStr.indexOf(dateStr)), standLine.getDatetime());
                standLine.setKeyNode(keyNode);
            }
            // 找到关键节点信息，并将非关键节点信息之外的工作内容信息提取出来
            int flagIndex = oneLineLog.indexOf(flagStr);
            String workInfo = oneLineLog.substring(0, flagIndex);
            standLine.setWorkInfo(extractWorkInfo(workInfo));
            return standLine;
        }
        standLine.setWorkInfo(extractWorkInfo(oneLineLog));
        standLine.setErrorInfo("无关键时间节点信息");
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
            workInfo = workInfo.replaceAll("^\\d{1,2}[\\.、]", "");
        }
        return workInfoStandard(workInfo);
    }

    /**
     * 工作内容信息标准化
     * @param workInfo 工作内容信息
     * @return 标准化后的工作内容信息
     */
    private String workInfoStandard(String workInfo) {
        return workInfo.replace("[","【")
                .replace("]", "】");
    }
}
