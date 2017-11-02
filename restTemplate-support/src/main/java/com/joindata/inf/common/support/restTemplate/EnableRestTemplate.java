package com.joindata.inf.common.support.restTemplate;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

import java.lang.annotation.*;

/**
 * Created by likanghua on 2017/9/26.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "RDBMS ORM 支持")
public @interface EnableRestTemplate {


}
