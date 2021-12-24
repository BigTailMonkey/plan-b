package com.btm.planb.timeutil;

public class Constant {

    private Constant() {}

    public static final String YEAR_FORMAT = "([1-9]\\d{0,3})";
    public static final String MONTH_FORMAT = "(0?[1-9]|1[012])";
    public static final String DAY_FORMAT = "(3[01]|[12]\\d|0?[1-9])";

    public static final String DATE_FORMAT = YEAR_FORMAT + "?[-年/\\.]?"+MONTH_FORMAT+"+[-月/\\.]?"+DAY_FORMAT+"+[日号]?";
    public static final String DATE_FORMAT_IGNORE_DECIMAL = YEAR_FORMAT +"?[-年/]?"+MONTH_FORMAT+"+[-月/]?"+DAY_FORMAT+"+[日号]?";

}
