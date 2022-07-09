package com.btm.planb.diffobject.generate.meta;

import com.btm.planb.diffobject.generate.meta.Specifies;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Repeatable(value = Specifies.class)
public @interface Specify {

    /**
     * 数据来源属性的名称，即传参对象中的字段名称<br/>
     * 即：注解所在的方法生命的返回值类型不同的类的字段名称
     * @return 名称
     */
    String source();

    /**
     * 数据写入目标对象的属性名称，即比较对象中的字段名称<br/>
     * 即：注解所在的方法生命的返回值类型相同的类的字段名称
     * @return 名称
     */
    String target();

}
