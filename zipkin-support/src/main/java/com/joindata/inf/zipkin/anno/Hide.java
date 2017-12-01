package com.joindata.inf.zipkin.anno;

import java.lang.annotation.*;

/**
 * Created by Rayee on 2017/12/1.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Hide {

    boolean value() default true;

}
