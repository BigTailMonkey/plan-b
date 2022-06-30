package com.btm.planb.streamcondition.core.impl;

import com.btm.planb.streamcondition.core.AbstractCondition;
import com.btm.planb.streamcondition.ConditionBuilder;

import java.util.Arrays;

public class ConditionType extends AbstractCondition<String> {

    public ConditionType(ConditionBuilder conditionBuilder) {
        super(conditionBuilder);
    }

    @Override
    protected String fileName() {
        return "type";
    }

    @Override
    protected String value(String s) {
        return s;
    }

    @Override
    protected String values(String... t) {
        String s = Arrays.toString(t);
        if (s.length() > 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
