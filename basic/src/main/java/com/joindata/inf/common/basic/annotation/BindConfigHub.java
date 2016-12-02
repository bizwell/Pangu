package com.joindata.inf.common.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BindConfigHub
{
    Class<? extends AbstractConfigHub> value();
}
