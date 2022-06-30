package com.btm.planb.streamcondition;

import com.btm.planb.streamcondition.core.ConditionBuilder;

public class Demo {

    public static void main(String[] args) {
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        String build = conditionBuilder
                .use("price").gt(100)
                .and()
                .use("type").eq("手机")
                .or(builder ->
                    builder.use("price").lte(50)
                            .or()
                            .use("price").gte(60)
                            .and()
                            .use("type").eq("无人机"))
                .build();
        System.out.println(build);
    }

}
