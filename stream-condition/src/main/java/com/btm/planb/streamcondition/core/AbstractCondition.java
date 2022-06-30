package com.btm.planb.streamcondition.core;

import com.btm.planb.streamcondition.core.constant.NodeType;
import com.btm.planb.streamcondition.core.constant.SymbolType;
import com.btm.planb.streamcondition.core.tree.AbstractNode;
import com.btm.planb.streamcondition.core.tree.LogicNode;

import java.util.Objects;

public abstract class AbstractCondition<T> {

    protected final ConditionBuilder conditionBuilder;

    protected AbstractCondition(ConditionBuilder conditionBuilder) {
        this.conditionBuilder = conditionBuilder;
    }

    public ConditionBuilder eq(T t) {
        LogicNode<T> newNode = eqInner(t);
        newNode.setSymbolType(SymbolType.EQ);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    public ConditionBuilder gt(T t) {
        LogicNode<T> newNode = eqInner(t);
        newNode.setSymbolType(SymbolType.GT);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    public ConditionBuilder gte(T t) {
        LogicNode<T> newNode = eqInner(t);
        newNode.setSymbolType(SymbolType.GTE);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    public ConditionBuilder lt(T t) {
        LogicNode<T> newNode = eqInner(t);
        newNode.setSymbolType(SymbolType.LT);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    public ConditionBuilder lte(T t) {
        LogicNode<T> newNode = eqInner(t);
        newNode.setSymbolType(SymbolType.LTE);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    private void checkAndSet(LogicNode<T> newNode) {
        AbstractNode current = this.conditionBuilder.getCurrent();
        if (Objects.isNull(current)) {
            this.conditionBuilder.setCurrent(newNode);
        } else if (NodeType.RELATION.equals(current.getNodeType())) {
            current.setRightNode(newNode);
        } else {
            throw new RuntimeException("逻辑关系编写错误");
        }
    }

    protected abstract LogicNode<T> eqInner(T t);
}
