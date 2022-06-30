package com.btm.planb.streamcondition;

import com.btm.planb.streamcondition.core.ConditionBuilder;

public class AllDemoApplication {

    public static void main(String[] args) {
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        String build = conditionBuilder
                .price().gt(100)
                .and()
                .type().eq("手机")
                .or(builder ->
                    builder.price().lte(50)
                            .or()
                            .price().gte(60)
                            .and()
                            .type().eq("无人机"))
                .build();
        System.out.println(build);
    }

}
