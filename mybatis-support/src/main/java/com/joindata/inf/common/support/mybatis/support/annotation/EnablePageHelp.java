package com.joindata.inf.common.support.mybatis.support.annotation;

/**
 * Created by likanghua on 2017/9/27.
 */
public @interface EnablePageHelp {
    PageHelpProperties[] value() default {};

    public static @interface PageHelpProperties {
        String key();

        String value();
    }
}
