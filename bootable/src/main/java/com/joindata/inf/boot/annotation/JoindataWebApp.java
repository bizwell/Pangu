/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.joindata.inf.boot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

/**
 * 定义这是嘉银数据的应用<br />
 * <i>把 @SpringBootConfiguration 改吧改吧就成了</i>
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午4:33:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
public @interface JoindataWebApp
{
    /** 应用 ID */
    String id();

    /** 应用版本号 */
    String version();

    /** 端口号，默认 8080 */
    int port() default 8080;

    /** 用什么样的 Web 容器，默认是 Jetty（目前你也没得选 →_→） */
    Server container() default Server.JETTY;

    public enum Server
    {
        JETTY
    }
}