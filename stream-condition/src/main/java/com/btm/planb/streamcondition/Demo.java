package com.btm.planb.streamcondition;

import com.btm.planb.streamcondition.core.ConditionBuilder;

public class Demo {

    public static void main(String[] args) {
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        String condition = conditionBuilder
                .price().eq(100)
                .and()
                .type().in("苹果")
                .or(builder -> builder
                        .price().eq(200)
                        .and()
                        .type().eq("安卓"))
                .build();
        System.out.println(condition);
    }

}
