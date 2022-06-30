package com.btm.planb.streamcondition.core;

import com.btm.planb.streamcondition.core.constant.NodeType;
import com.btm.planb.streamcondition.core.constant.RelationType;
import com.btm.planb.streamcondition.core.impl.ConditionPrice;
import com.btm.planb.streamcondition.core.impl.ConditionType;
import com.btm.planb.streamcondition.core.tree.AbstractNode;
import com.btm.planb.streamcondition.core.tree.LogicNode;
import com.btm.planb.streamcondition.core.tree.RelationNode;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class ConditionBuilder {

    private AbstractNode current;

    public ConditionBuilder() {
    }

    public AbstractNode getCurrent() {
        return current;
    }

    public void setCurrent(LogicNode logicNode) {
        this.current = logicNode;
    }

    public ConditionBuilder and(UnaryOperator<ConditionBuilder> operate) {
        checkCurrentNodeIsNotNull();
        RelationNode node = new RelationNode(RelationType.AND, true);
        node.setLeftNode(this.current);
        this.current = node;
        ConditionBuilder whole = operate.apply(new ConditionBuilder());
        this.current.setRightNode(whole.getCurrent());
        return this;
    }

    public ConditionBuilder or(UnaryOperator<ConditionBuilder> operate) {
        checkCurrentNodeIsNotNull();
        RelationNode node = new RelationNode(RelationType.OR, true);
        node.setLeftNode(this.current);
        this.current = node;
        ConditionBuilder whole = operate.apply(new ConditionBuilder());
        this.current.setRightNode(whole.getCurrent());
        return this;
    }

    public ConditionBuilder and() {
        checkCurrentNodeIsNotNull();
        RelationNode relationNode = new RelationNode(RelationType.AND, false);
        relationNode.setLeftNode(this.current);
        this.current = relationNode;
        return this;
    }

    public ConditionBuilder or() {
        checkCurrentNodeIsNotNull();
        RelationNode relationNode = new RelationNode(RelationType.OR, false);
        relationNode.setLeftNode(this.current);
        this.current = relationNode;
        return this;
    }

    public void checkCurrentNodeIsNotNull() {
        if (Objects.isNull(this.current)) {
            throw new RuntimeException("逻辑关系错误");
        }
    }

    public String build() {
        if (Objects.isNull(this.current) || Objects.isNull(this.current.getRightNode())) {
            throw new RuntimeException("逻辑关系不完整");
        }
        return this.current.toString();
    }

    public ConditionPrice price() {
        if (Objects.nonNull(this.current) && NodeType.FIELD.equals(this.current.getNodeType())) {
            throw new RuntimeException("逻辑关系错误");
        }
        return new ConditionPrice( this);
    }

    public ConditionType type() {
        if (Objects.nonNull(this.current) && NodeType.FIELD.equals(this.current.getNodeType())) {
            throw new RuntimeException("逻辑关系错误");
        }
        return new ConditionType(this);
    }
}
