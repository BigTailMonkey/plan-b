package com.btm.planb.parallel.framework.model;

/**
 * 批处理参数<br/>
 * 用于数据收集阶段，可基于此中数据扫描新的待处理数据
 */
public class BatchDataInfo {

    /**
     * 一批次处理的数据中最大的ID
     */
    private long maxId;

    /**
     * 一批次处理的数据量
     */
    private long countNumber;

    public BatchDataInfo() {
        this.maxId = -1;
        this.countNumber = 0;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public long getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(long countNumber) {
        this.countNumber = countNumber;
    }
}
