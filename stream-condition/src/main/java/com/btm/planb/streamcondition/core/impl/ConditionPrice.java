package com.btm.planb.streamcondition.core.impl;

import com.btm.planb.streamcondition.core.AbstractCondition;
import com.btm.planb.streamcondition.ConditionBuilder;

import java.util.Arrays;


public class ConditionPrice extends AbstractCondition<Integer> {

    public ConditionPrice(ConditionBuilder conditionBuilder) {
        super(conditionBuilder);
    }

    @Override
    protected String fileName() {
        return "price";
    }

    @Override
    protected String value(Integer integer) {
        return String.valueOf(integer);
    }

    @Override
    protected String values(Integer... t) {
        String s = Arrays.toString(t);
        if (s.length() > 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
