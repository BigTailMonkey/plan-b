package com.btm.planb.worklogstatistic.keyNode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class INode {

    /** 节点描述的命名，正式的节点命名 **/
    protected String nodeDesc;
    /** 节点描述的命名，用户输入的可匹配的别名 **/
    protected String nodeDescAlias;
    /** 可供匹配使用的节点描述的备选别名 **/
    protected List<String> nodeAliasArr;
    protected INode next;
    protected INode previous;
    /** 原始时间节点信息 **/
    private String keyInfo;

    public String getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(String keyInfo) {
        this.keyInfo = keyInfo;
    }

    public String getNodeDescAlias() {
        return nodeDescAlias;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public String getNextNodeDesc() {
        return Objects.nonNull(next) ? next.nodeDesc : "";
    }

    public String getPreviousNodeDesc() {
        return Objects.nonNull(previous) ? previous.nodeDesc : "";
    }

    public String getNextNodeDescAlias() {
        return Objects.nonNull(next) ? next.nodeDescAlias : "";
    }

    public String getPreviousNodeDescAlias() {
        return Objects.nonNull(previous) ? previous.nodeDescAlias : "";
    }

    protected INode(INode previous, String nodeDesc, String... nodeAlias) {
        this.previous = previous;
        this.nodeDesc = nodeDesc;
        this.nodeAliasArr = new ArrayList<>(Arrays.asList(nodeAlias));
        this.nodeAliasArr.add(nodeDesc);
        if (Objects.nonNull(previous)) {
            this.previous.next = this;
        }
    }

    /**
     * 根据关键词匹配是否已经到达了当前节点
     * @param keyNodeInfo
     * @return
     */
    protected INode isArrived(String keyNodeInfo) {
        for (String nodeDesc : nodeAliasArr) {
            if (keyNodeInfo.contains(nodeDesc)) {
                this.nodeDescAlias = nodeDesc;
                return this;
            }
        }
        return Objects.nonNull(this.next) ? next.isArrived(keyNodeInfo) : null;
    }

    /**
     * 根据时间判断是否已经到达了当前节点
     * @param dateInfo
     * @return
     */
    public INode isArrived(LocalDate dateInfo) {
        if (Objects.isNull(dateInfo)) {
            return this;
        } else if (dateInfo.isAfter(LocalDate.now())) {
            return Objects.isNull(this.previous) ? this : this.previous;
        } else {
            return this;
        }
    }

}
