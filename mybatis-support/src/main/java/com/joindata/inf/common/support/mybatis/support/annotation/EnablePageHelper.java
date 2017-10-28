package com.joindata.inf.common.support.mybatis.support.annotation;

import com.joindata.inf.common.basic.annotation.JoindataComponent;
import com.joindata.inf.common.support.mybatis.ConfigHub;

import java.lang.annotation.*;

/**
 * Created by likanghua on 2017/9/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "RDBMS ORM 支持")
public @interface EnablePageHelper {
    PageHelpProperties[] value() default {};

    public static @interface PageHelpProperties {
        String key();

        String value();
    }
}
