package com.joindata.inf.boot.sterotype.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标记 SpringMVC 入参解析器
 * 
 * @author Muyv
 * @date 2016年2月18日 下午10:03:22
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebArgResolver
{

}
