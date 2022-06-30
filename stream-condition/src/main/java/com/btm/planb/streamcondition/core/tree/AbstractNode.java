package com.btm.planb.streamcondition.core.tree;

import com.btm.planb.streamcondition.core.constant.NodeType;

public abstract class AbstractNode {

    private AbstractNode parent;
    private AbstractNode leftNode;
    private AbstractNode rightNode;
    protected NodeType nodeType;


    public void setParent(AbstractNode parent) {
        this.parent = parent;
    }

    public AbstractNode getParent() {
        return this.parent;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public AbstractNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(AbstractNode leftNode) {
        this.leftNode = leftNode;
    }

    public AbstractNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(AbstractNode rightNode) {
        this.rightNode = rightNode;
    }
}
