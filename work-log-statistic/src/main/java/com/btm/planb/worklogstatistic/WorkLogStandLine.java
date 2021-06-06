package com.btm.planb.worklogstatistic;

import com.btm.planb.exportexcel.ExcelHeader;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工作日志的标准行所含有的内容
 */
public class WorkLogStandLine implements Comparable<WorkLogStandLine> {

    private final String demandNumberRegex;

    /**
     * @param demandNumberRegex 问题编号格式
     */
    public WorkLogStandLine(String demandNumberRegex) {
        this.demandNumberRegex = demandNumberRegex;
    }

    // 原始内容
    private String sourceStr;

    // 时间
    private String datetime;

    // 内容
    private String workInfo;

    // 需求编号
    private String demandNumber;

    // 关键节点信息
    private String flag;

    // 关键节点信息的原始数据
    // 关键节点信息要位于日志的结尾，使用括号标示并要含有时间信息
    private String flagSourceInfo;

    // 错误信息
    private String errorInfo;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @ExcelHeader("工作内容")
    public String getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(String workInfo) {
        this.workInfo = workInfo;
        Pattern pattern = Pattern.compile(demandNumberRegex);
        Matcher matcher = pattern.matcher(workInfo);
        if (matcher.find()) {
            this.demandNumber = matcher.group();
        }
    }

    @ExcelHeader("TB号")
    public String getDemandNumber() {
        return Objects.isNull(demandNumber) ? "" : demandNumber;
    }

    @ExcelHeader("状态")
    public String getFlag() {
        return Objects.isNull(flag) ? "" : flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSourceStr() {
        return sourceStr;
    }

    public void setSourceStr(String sourceStr) {
        this.sourceStr = sourceStr;
    }

    @ExcelHeader("异常信息")
    public String getErrorInfo() {
        return Objects.isNull(errorInfo) ? "" : errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getFlagSourceInfo() {
        return Objects.isNull(this.flagSourceInfo) ? "" : this.flagSourceInfo;
    }

    public void setFlagSourceInfo(String flagSourceInfo) {
        this.flagSourceInfo = flagSourceInfo;
    }

    @ExcelHeader("备注")
    public String remark() {
        if (Objects.nonNull(datetime) && Objects.nonNull(flag)) {
            return this.datetime + " " + this.flag;
        }
        return getFlagSourceInfo();
    }

    @Override
    public int compareTo(WorkLogStandLine other) {
        if (Objects.isNull(demandNumber) && Objects.isNull(other.demandNumber)) {
            return 0;
        } else if (Objects.isNull(demandNumber)) {
            return -1;
        } else if (Objects.isNull(other.demandNumber)) {
            return 1;
        } else {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(this.getDemandNumber());
            Matcher otherMatcher = pattern.matcher(other.getDemandNumber());
            if (matcher.find() && otherMatcher.find()) {
                return Integer.parseInt(matcher.group()) - Integer.parseInt(otherMatcher.group());
            } else if (!matcher.find()) {
                return -1;
            } else if (!otherMatcher.find()) {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public String toString() {
        return "WorkLogStandLine{" +
                "sourceStr='" + sourceStr + '\'' +
                ", Datetime='" + datetime + '\'' +
                ", workInfo='" + workInfo + '\'' +
                ", flag='" + flag + '\'' +
                ", flagSourceInfo='" + flagSourceInfo + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkLogStandLine standLine = (WorkLogStandLine) o;
        return Objects.equals(sourceStr, standLine.sourceStr) && Objects.equals(datetime, standLine.datetime) && Objects.equals(workInfo, standLine.workInfo) && Objects.equals(flag, standLine.flag) && Objects.equals(flagSourceInfo, standLine.flagSourceInfo) && Objects.equals(errorInfo, standLine.errorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceStr, datetime, workInfo, flag, flagSourceInfo, errorInfo);
    }
}
