package com.btm.planb.worklogstatistic;

import java.util.Objects;

public class LogLineProperties {

    @PropertiesName("main.programs")
    private String[] mainPrograms;

    @PropertiesName("program.groups")
    private String[] programGroups;

    /**
     * 是否为重点项目,待检测文本中含有重点项目的关键词即被认定为重点项目
     *
     * @param text 待检测文本
     * @return true:是重点项目；false：非重点项目
     */
    public boolean isMainProgram(String text) {
        if (Objects.isNull(text)) {
            return false;
        }
        for (String mainProgram : mainPrograms) {
            if (text.contains(mainProgram)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测待检测文本是否是项目集标识
     *
     * @param text 待检测文本
     * @return
     */
    public boolean isProgramGroup(String text) {
        if (Objects.isNull(text)) {
            return false;
        }
        for (String programGroup : programGroups) {
            if (text.equals(programGroup)) {
                return true;
            }
        }
        return false;
    }
}
