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
    private final ConditionManager conditionManager = new ConditionManager();

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

    public void checkCurrentNodeIsField() {
        if (Objects.nonNull(this.current) && NodeType.FIELD.equals(this.current.getNodeType())) {
            throw new RuntimeException("逻辑关系错误");
        }
    }

    /**可支持更对条件对象，但对编译器不友好**/
    public AbstractCondition use(String name) {
        checkCurrentNodeIsField();
        try {
            Class<? extends AbstractCondition> t = conditionManager.get(name);
            return t.getConstructor(ConditionBuilder.class).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**推荐使用一下方式，但要注意不要超过java文件限制**/
    public ConditionPrice price() {
        checkCurrentNodeIsField();
        return new ConditionPrice(this);
    }

    public ConditionType type() {
        checkCurrentNodeIsField();
        return new ConditionType(this);
    }
}
