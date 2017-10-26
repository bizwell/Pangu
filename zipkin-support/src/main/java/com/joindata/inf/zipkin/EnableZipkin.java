package com.joindata.inf.zipkin;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

import java.lang.annotation.*;

/**
 * Created by Rayee on 2017/10/23.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "dubbo调用链跟踪")
public @interface EnableZipkin {


}
