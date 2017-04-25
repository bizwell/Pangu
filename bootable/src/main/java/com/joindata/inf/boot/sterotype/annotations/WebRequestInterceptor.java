package com.joindata.inf.boot.sterotype.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为拦截器<br />
 * <i>并可以声明拦截器中要拦截或不拦截的路径的注解</i>
 * 
 * @author 宋翔
 * @date 2015年12月16日 下午3:01:10
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebRequestInterceptor
{
    /** 拦截的路径 */
    String[] value() default {"/**"};

    /** 拦截的路径 */
    String[] include() default {"/**"};

    /** 不拦截的路径 */
    String[] exclude() default {};
}
