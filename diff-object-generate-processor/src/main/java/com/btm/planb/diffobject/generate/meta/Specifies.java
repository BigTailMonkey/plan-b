package com.btm.planb.diffobject.generate.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Specifies {

    Specify[] value();
}
