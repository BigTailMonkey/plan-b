package com.btm.planb.timeutil.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class PeriodDetectInfoModel implements IPeriod, Comparable<PeriodDetectInfoModel> {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public PeriodDetectInfoModel(LocalDateTime startTime, LocalDateTime endTime) {
        if (Objects.isNull(startTime) || Objects.isNull(endTime)) {
            throw new RuntimeException("请提供有效的时间");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public int compareTo(PeriodDetectInfoModel o) {
        if (Objects.isNull(o)) {
            return -1;
        }
        if (this.startTime.isBefore(o.startTime)) {
            return -1;
        } else if (this.startTime.isAfter(o.startTime)) {
            return 1;
        } else {
            return this.endTime.isBefore(o.endTime) ? -1 : 0 ;
        }
    }
}
