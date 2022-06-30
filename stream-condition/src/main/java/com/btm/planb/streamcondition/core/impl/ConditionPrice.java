package com.btm.planb.streamcondition.core.impl;

import com.btm.planb.streamcondition.core.AbstractCondition;
import com.btm.planb.streamcondition.core.ConditionBuilder;
import com.btm.planb.streamcondition.core.tree.LogicNode;


public class ConditionPrice extends AbstractCondition<Integer> {

    public ConditionPrice(ConditionBuilder conditionBuilder) {
        super(conditionBuilder);
    }

    @Override
    protected LogicNode<Integer> eqInner(Integer integer) {
        return new LogicNode<>("price",integer);
    }
}
