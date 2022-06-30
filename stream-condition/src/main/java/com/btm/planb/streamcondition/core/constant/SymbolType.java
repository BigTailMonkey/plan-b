package com.btm.planb.streamcondition.core.constant;

public enum SymbolType {

    EQ(" = "),
    GT(" > "),
    GTE(" >= "),
    LT(" < "),
    LTE(" <= "),
    IN(" in "),
    ;
    private String symbol;

    private SymbolType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
