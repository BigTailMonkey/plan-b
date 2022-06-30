package com.btm.planb.streamcondition.core.tree;

import com.btm.planb.streamcondition.core.constant.NodeType;
import com.btm.planb.streamcondition.core.constant.SymbolType;

public class LogicNode extends AbstractNode {

    private final String fieldName;
    private final String value;
    private SymbolType symbolType;

    public LogicNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
        this.nodeType = NodeType.FIELD;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    @Override
    public String toString() {
        return fieldName + symbolType.getSymbolStart() + value + symbolType.getSymbolEnd();
    }
}
