package com.btm.planb.exportexcel;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelHeader {

    /**
     * 表头
     * @return
     */
    String value() default "";

}
