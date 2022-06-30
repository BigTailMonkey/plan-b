package com.btm.planb.streamcondition.core.constant;

public enum RelationType {

    AND(" and "),
    OR(" or "),
    ;

    private String relation;

    private RelationType(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }
}
