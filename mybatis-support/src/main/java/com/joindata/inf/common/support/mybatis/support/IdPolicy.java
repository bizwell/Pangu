package com.joindata.inf.common.support.mybatis.support;

import javax.persistence.Inheritance;
import java.lang.annotation.*;

/**
 * Created by likanghua on 2017/9/27.
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inheritance
public @interface IdPolicy {
    Policy value() default Policy.UUID;

    public static enum Policy {
        UUID,
        ZKID
    }
}
