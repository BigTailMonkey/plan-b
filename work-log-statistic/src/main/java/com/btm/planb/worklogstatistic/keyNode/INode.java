package com.btm.planb.worklogstatistic.keyNode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class INode {

    /** 节点描述的命名 **/
    protected String nodeDesc;
    /** 节点描述的别名 **/
    protected List<String> nodeAliasArr;
    protected INode next;
    protected INode previous;

    public String getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(String keyInfo) {
        this.keyInfo = keyInfo;
    }

    public String getNextNodeDesc() {
        return Objects.nonNull(next) ? next.nodeDesc : "";
    }

    private String keyInfo;

    protected INode(INode previous, String nodeDesc, String... nodeAlias) {
        this.previous = previous;
        this.nodeDesc = nodeDesc;
        this.nodeAliasArr = new ArrayList<>(Arrays.asList(nodeAlias));
        this.nodeAliasArr.add(nodeDesc);
        if (Objects.nonNull(previous)) {
            this.previous.next = this;
        }
    }

    protected INode isArrived(String keyNodeInfo) {
        for (String nodeDesc : nodeAliasArr) {
            if (keyNodeInfo.contains(nodeDesc)) {
                return this;
            }
        }
        return Objects.nonNull(this.next) ? next.isArrived(keyNodeInfo) : null;
    }

    public INode isArrived(LocalDate dateInfo) {
        if (Objects.isNull(dateInfo)) {
            return this;
        } else if (dateInfo.isBefore(LocalDate.now())) {
            return this;
        } else {
            return Objects.isNull(this.previous) ? this : this.previous;
        }
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

}
