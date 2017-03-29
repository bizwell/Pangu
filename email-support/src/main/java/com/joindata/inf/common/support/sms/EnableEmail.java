package com.joindata.inf.common.support.sms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;
import com.joindata.inf.common.support.sms.cst.SmsSystemId;

/**
 * 启用短信服务
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 21, 2017 5:52:14 PM
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