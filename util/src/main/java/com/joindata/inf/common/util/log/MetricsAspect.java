package com.joindata.inf.common.util.log;

import com.joindata.inf.common.basic.exceptions.BizException;
import com.joindata.inf.common.util.basic.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志切面类
 *
 * @author <a href="mailto:zhugongyi@niwodai.net">诸龚毅</a>
 * @date 2017年08月04日15:40:52
 */
@Aspect
@Configuration
public class MetricsAspect {

    public MetricsAspect() {
    }

    @Around("@annotation(com.joindata.inf.common.util.log.Metrics))")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();

        Logger logger = getLogger(signature);

        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        if (metrics == null) {
            metrics = defaultMetrics;
        }
        String metricsName = signature.getDeclaringTypeName() + " - " + signature.getName();
        String inputJson = null;
        //记录入参
        if (metrics.logInput()) {
            inputJson = JsonUtil.toJSON(point.getArgs());
            logger.info("{} 调用【{}】- 参数为【{}】", metrics.desc(), metricsName, inputJson);
        }
        //执行业务
        try {
            Object result = point.proceed();
            //记录返回值
            if (metrics.logOutput()) {
                logger.info("{} 调用【{}】成功 - 返回值为【{}】", metrics.desc(), metricsName, JsonUtil.toJSON(result));
            }
            return result;
        } catch (BizException be) {
            if (StringUtils.isEmpty(inputJson)) {
                inputJson = JsonUtil.toJSON(point.getArgs());
            }
            logger.error("{} 调用【{}】业务异常 - 【参数 = {}】-【异常信息:code = {}, msg = {}】", metrics.desc(), metricsName, inputJson, be.getErrorEntity().getCode(), be.getErrorEntity().getMessage());
            throw be;
        } catch (Exception e) {
            if (StringUtils.isEmpty(inputJson)) {
                inputJson = JsonUtil.toJSON(point.getArgs());
            }
            logger.error("{} 调用【{}】系统异常 - 【参数={}】", metrics.desc(), metricsName, inputJson, e);
            throw e;
        }
    }

    private Logger getLogger(MethodSignature signature){
        Logger logger = cachedLoggers.get(signature.getMethod().getDeclaringClass());
        if(logger == null){
            logger = Logger.newLogger(signature.getMethod().getDeclaringClass());
            cachedLoggers.put(signature.getMethod().getDeclaringClass(), logger);
        }
        return logger;
    }

    private static ConcurrentHashMap<Class, Logger> cachedLoggers = new ConcurrentHashMap<>();

    private static final Metrics defaultMetrics = new Metrics() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String desc() {
            return "";
        }

        @Override
        public boolean logInput() {
            return true;
        }

        @Override
        public boolean logOutput() {
            return true;
        }

        @Override
        public boolean ignoreBusErr() {
            return false;
        }

        @Override
        public boolean ignoreSysErr() {
            return false;
        }

        @Override
        public boolean ignoreError() {
            return false;
        }
    };

}
