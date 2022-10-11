package com.btm.planb.swaggerenum;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelEnumField {

    /**
     * 指定枚举中所使用的枚举值的字段名称，即接口中实际传入、使用的值的字段名称
     * @return 枚举值字段名称
     */
    String value() default "value";

    /**
     * 指定枚举类中所使用的枚举描述字段的名称，若为空，则默认使用枚举类的名称
     * @return 枚举描述字段的名称
     */
    String desc() default "";

    /**
     * 实际使用的枚举类
     * @return 枚举类
     */
    Class<? extends Enum> type();
}
