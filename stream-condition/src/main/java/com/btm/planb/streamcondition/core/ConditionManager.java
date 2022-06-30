package com.btm.planb.streamcondition.core;

import com.btm.planb.streamcondition.core.impl.ConditionPrice;
import com.btm.planb.streamcondition.core.impl.ConditionType;

import java.util.HashMap;
import java.util.Map;

public class ConditionManager {

    private static final Map<String, Class> conditions = new HashMap<>();

    static {
        conditions.put("price", ConditionPrice.class);
        conditions.put("type", ConditionType.class);
    }

    public Class get(String name) {
        if (conditions.containsKey(name)) {
            return conditions.get(name);
        }
        throw new IllegalArgumentException("不支持的条件："+name);
    }

}
