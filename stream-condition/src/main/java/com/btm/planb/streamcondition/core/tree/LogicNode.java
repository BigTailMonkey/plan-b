package com.btm.planb.streamcondition.core.tree;


import com.btm.planb.streamcondition.core.constant.NodeType;
import com.btm.planb.streamcondition.core.constant.SymbolType;

public class LogicNode<T> extends AbstractNode {

    private final String fieldName;
    private final T value;
    private SymbolType symbolType;

    public LogicNode(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
        this.nodeType = NodeType.FIELD;
    }

    public Object getValue() {
        return this.value;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    @Override
    public String toString() {
        return fieldName + symbolType.getSymbol() + value;
    }
}
