package com.btm.planb.streamcondition.core.tree;

import com.btm.planb.streamcondition.core.constant.NodeType;
import com.btm.planb.streamcondition.core.constant.RelationType;

public class RelationNode extends AbstractNode {

    private RelationType relationType;
    // 是否将左右子树各看作一个整体
    private final boolean whole;

    public RelationNode(RelationType relationType, boolean whole) {
        this.nodeType = NodeType.RELATION;
        this.relationType = relationType;
        this.whole = whole;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public boolean isWhole() {
        return whole;
    }

    @Override
    public String toString() {
        if (whole) {
            return "( " + this.getLeftNode().toString() + " )" +
                    relationType.getRelation() +
                    "( " + this.getRightNode().toString() +" )";
        } else {
            return this.getLeftNode().toString() +
                    relationType.getRelation() +
                    this.getRightNode().toString();
        }
    }
}
