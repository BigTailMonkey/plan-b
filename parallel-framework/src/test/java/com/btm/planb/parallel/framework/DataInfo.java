package com.btm.planb.parallel.framework;

import com.btm.planb.parallel.framework.model.SourceDataDefinition;

public class DataInfo implements SourceDataDefinition {

    private final long id;

    private final String name;

    public DataInfo(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

}
