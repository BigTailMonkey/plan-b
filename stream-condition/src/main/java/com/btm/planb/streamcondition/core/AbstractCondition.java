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
        LogicNode newNode = new LogicNode(fileName(), value(t));
        newNode.setSymbolType(SymbolType.EQ);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    public ConditionBuilder in(T... t) {
        LogicNode newNode = new LogicNode(fileName(), values(t));
        newNode.setSymbolType(SymbolType.IN);
        checkAndSet(newNode);
        return this.conditionBuilder;
    }

    private void checkAndSet(LogicNode newNode) {
        AbstractNode current = this.conditionBuilder.getCurrent();
        if (Objects.isNull(current)) {
            this.conditionBuilder.setCurrent(newNode);
        } else if (NodeType.RELATION.equals(current.getNodeType())) {
            current.setRightNode(newNode);
        } else {
            throw new RuntimeException("逻辑关系编写错误");
        }
    }

    protected abstract String value(T t);

    protected abstract String values(T... t);

    protected abstract String fileName();
}
