package com.joindata.inf.common.support.email;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;
import com.joindata.inf.common.support.email.cst.SmsSystemId;

/**
 * 
 * 启用邮件服务
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017-03-29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "邮件服务")
public @interface EnableEmail
{
    /**
     * 在短信系统中的 AppID
     * 
     * @see SmsSystemId
     */
    String systemId();
}