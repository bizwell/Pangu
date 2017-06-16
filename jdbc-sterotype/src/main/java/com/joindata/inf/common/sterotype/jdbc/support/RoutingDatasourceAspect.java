package com.joindata.inf.common.sterotype.jdbc.support;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.joindata.inf.common.sterotype.jdbc.annotation.Datasource;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

@Aspect
public class RoutingDatasourceAspect
{
    private static final Logger log = Logger.get();

    private static Map<MethodSignature, String> MethodDs = CollectionUtil.newMap();

    @Pointcut("execution(* *(..))")
    public void method()
    {
    }

    @Around("method() && @annotation(com.joindata.inf.common.sterotype.jdbc.annotation.Datasource)")
    public Object changeDatasourceMethod(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature sig = (MethodSignature)joinPoint.getSignature();
        if(!MethodDs.containsKey(sig))
        {
            MethodDs.put(sig, sig.getMethod().getAnnotation(Datasource.class).value());
        }

        log.debug("方法 {} 包含 @Datasource，将切换到数据源: {}", sig, MethodDs.get(sig));

        DataSourceRoutingHolder.useDatasource(MethodDs.get(sig));
        Object ret = joinPoint.proceed();
        DataSourceRoutingHolder.releaseDatasource(MethodDs.get(sig));

        return ret;
    }

    @Around("method() && @within(com.joindata.inf.common.sterotype.jdbc.annotation.Datasource)")
    public Object changeDatasourceClass(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature sig = (MethodSignature)joinPoint.getSignature();

        if(sig.getMethod().getAnnotation(Datasource.class) != null)
        {
            return joinPoint.proceed();
        }

        if(!MethodDs.containsKey(sig))
        {
            MethodDs.put(sig, joinPoint.getTarget().getClass().getAnnotation(Datasource.class).value());
        }

        log.debug("方法 {} 所在类包含 @Datasource，将切换到数据源: {}", sig, MethodDs.get(sig));

        DataSourceRoutingHolder.useDatasource(MethodDs.get(sig));
        Object ret = joinPoint.proceed();
        DataSourceRoutingHolder.releaseDatasource(MethodDs.get(sig));

        return ret;
    }
}
