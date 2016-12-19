package com.joindata.inf.common.support.sso;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.joindata.inf.common.basic.annotation.JoindataComponent;
import com.joindata.inf.common.support.sso.stereotype.SsoInterceptor;

/**
 * 启用单点登录
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 5:58:09 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "SSO - 单点登录支持")
public @interface EnableSso
{
    /** 被排除的资源规则 */
    String[] ignorePattern() default {};

    /** 用户详情类 */
    Class<? extends UserDetailsService> userDetailService();

    /** 自定义登录拦截器 */
    Class<? extends SsoInterceptor>[] interceptor();
}