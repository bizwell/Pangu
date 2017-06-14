package com.joindata.inf.boot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * HTTP 消息转换器中 JSON 报文序列化设置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 14, 2017 2:42:48 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
public @interface MvcJsonSerializationFeature
{
    /**
     * 默认值：
     * <ul>
     * <li>SerializerFeature.DisableCircularReferenceDetect</li>
     * <li>SerializerFeature.SkipTransientField</li>
     * <li>SerializerFeature.WriteEnumUsingToString</li>
     * <li>SerializerFeature.WriteSlashAsSpecial</li>
     * </ul>
     * 
     * @see SerializerFeature FastJSON 序列化特征
     */
    SerializerFeature[] value();
}
