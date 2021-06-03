package com.btm.planb.worklogstatistic;

import com.btm.planb.exportexcel.ExcelHeader;

import java.util.Objects;

/**
 * 工作日志的标准行所含有的内容
 */
public class WorkLogStandLine {

    // 原始内容
    private String sourceStr;

    // 时间
    @ExcelHeader(value = "时间")
    private String Datetime;

    // 内容
    @ExcelHeader(value = "内容")
    private String workInfo;

    // 节点标志
    @ExcelHeader(value = "节点")
    private String flag;

    // 错误信息
    private String errorInfo;

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        this.Datetime = datetime;
    }

    public String getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(String workInfo) {
        this.workInfo = workInfo;
    }

    public String getFlag() {
        return flag;
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

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString() {
        return "WorkLogStandLine{" +
                "sourceStr='" + sourceStr + '\'' +
                ", Datetime='" + Datetime + '\'' +
                ", workInfo='" + workInfo + '\'' +
                ", flag='" + flag + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkLogStandLine standLine = (WorkLogStandLine) o;
        return Objects.equals(sourceStr, standLine.sourceStr)
                && Objects.equals(Datetime, standLine.Datetime)
                && Objects.equals(workInfo, standLine.workInfo)
                && Objects.equals(flag, standLine.flag)
                && Objects.equals(errorInfo, standLine.errorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceStr, Datetime, workInfo, flag, errorInfo);
    }
}
