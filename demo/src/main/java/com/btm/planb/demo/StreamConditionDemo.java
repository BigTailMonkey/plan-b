package com.btm.planb.demo;

import com.btm.planb.streamcondition.ConditionBuilder;

public class StreamConditionDemo {

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
