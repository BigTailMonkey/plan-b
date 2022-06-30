package com.btm.planb.streamcondition.core.impl;

import com.btm.planb.streamcondition.core.AbstractCondition;
import com.btm.planb.streamcondition.core.ConditionBuilder;
import com.btm.planb.streamcondition.core.tree.LogicNode;

public class ConditionType extends AbstractCondition<String> {

    public ConditionType(ConditionBuilder conditionBuilder) {
        super(conditionBuilder);
    }

    @Override
    protected LogicNode<String> eqInner(String s) {
        return new LogicNode<>("type", s);
    }
}
