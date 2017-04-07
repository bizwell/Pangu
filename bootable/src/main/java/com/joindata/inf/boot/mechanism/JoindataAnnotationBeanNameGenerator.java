package com.joindata.inf.boot.mechanism;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.annotation.FilterComponent;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 自定义的注解 Bean 名称生成器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月8日 下午1:17:13
 */
public class JoindataAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator
{
    private static final Logger log = Logger.get();

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
    {
        String beanName = null;
        Class<?> clz = ClassUtil.parseClass(definition.getBeanClassName());

        log.debug("创建 {} 的 Bean 名字, Class 对象是: {}", definition.getBeanClassName(), clz);
        if(clz.getAnnotation(Configuration.class) != null)
        {
            beanName = definition.getBeanClassName();
        }
        else if(clz.getAnnotation(FilterComponent.class) != null)
        {
            beanName = definition.getBeanClassName();
        }
        else
        {
            beanName = super.generateBeanName(definition, registry);
        }
        return beanName;
    }
}
