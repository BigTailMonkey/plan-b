package com.btm.planb.streamcondition.core.constant;

public enum SymbolType {

    EQ("=", ""),
    GT(">", ""),
    GTE(">=", ""),
    LT("<", ""),
    LTE("<=", ""),
    IN(" in (", ")"),
    NOT_IN(" not in (", ")")
    ;
    private String symbolStart;
    private String symbolEnd;

    private SymbolType(String symbolStart, String symbolEnd) {
        this.symbolStart = symbolStart;
        this.symbolEnd = symbolEnd;
    }

    public String getSymbolStart() {
        return symbolStart;
    }

    public String getSymbolEnd() {
        return symbolEnd;
    }
}
