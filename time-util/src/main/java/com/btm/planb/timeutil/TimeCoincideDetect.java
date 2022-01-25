package com.btm.planb.timeutil;

import com.btm.planb.timeutil.model.IPeriod;
import com.btm.planb.timeutil.model.PeriodDetectInfoModel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TimeCoincideDetect {

    /**
     * 判定时间段重叠情况
     * @param periods 时间段
     * @param overTime 某一时间段可以重叠的最大次数（不含基准的第一次），超过此限制则检测不过
     * @return 检测通过则返回null；检测不同通过则返回发现的第一个超过重叠最大次数的时间段
     */
    public String detect(List<IPeriod> periods, int overTime) {
        List<PeriodDetectInfoModel> infoModels = transform(periods);
        if (infoModels.isEmpty()) {
            throw new RuntimeException("请提供有效的时间段");
        }
        if (0 > overTime) {
            throw new RuntimeException("请指定一个不小于0的重叠最大次数");
        }
        Collections.sort(infoModels);
        int[] detectResult = new int[infoModels.size()];
        int index = 0;
        for (int i = 1;i < infoModels.size(); i ++) {
            PeriodDetectInfoModel current = infoModels.get(i);
            for (int j = i - 1; j >= index ; j--) {
                if (current.getStartTime().isBefore(infoModels.get(j).getEndTime())) {
                    detectResult[j] ++;
                    detectResult[i] ++;
                    if (detectResult[j] > overTime) {
                        return current.getStartTime() + " ~ " + periods.get(j).getEndTime() +"，重叠次数超过上限：" + overTime;
                    }
                } else if (current.getStartTime().isAfter(infoModels.get(j).getEndTime())) {
                    index = j;
                    break;
                }
            }
        }
        return null;
    }

    private List<PeriodDetectInfoModel> transform(List<IPeriod> periods) {
        if (Objects.isNull(periods) || periods.isEmpty()) {
            return Collections.emptyList();
        }
        return periods.stream().map(period -> new PeriodDetectInfoModel(period.getStartTime(), period.getEndTime())).collect(Collectors.toList());
    }
}
