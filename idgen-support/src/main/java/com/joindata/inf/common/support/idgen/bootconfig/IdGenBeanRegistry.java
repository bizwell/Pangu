package com.joindata.inf.common.support.idgen.bootconfig;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.idgen.annotation.Sequence;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 用于注册自定义的注解的 Bean 和注入到声明依赖的 Bean 中
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 24, 2017 4:19:44 PM
 */
@Configuration
public class IdGenBeanRegistry implements BeanDefinitionRegistryPostProcessor
{
    private static final Logger log = Logger.get();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        Set<Class<?>> clzsses = CollectionUtil.newHashSet();
        for(String beanName: registry.getBeanDefinitionNames())
        {
            BeanDefinition bean = registry.getBeanDefinition(beanName);

            // 只解析应用包里的
            if(!StringUtil.startsWith(bean.getBeanClassName(), BootInfoHolder.getAppPackage()))
            {
                continue;
            }

            Class<?> clz = ClassUtil.parseClass(bean.getBeanClassName());
            if(clz == null)
            {
                continue;
            }

            clzsses.add(clz);

        }

        for(Class<?> clz: clzsses)
        {
            // 注册 Sequence Bean
            {
                Set<Field> annoFlds = ClassUtil.getAnnotationFields(clz, Sequence.class);

                for(Field fld: annoFlds)
                {
                    Sequence sequence = fld.getAnnotation(Sequence.class);

                    String seqBeanName = fld.getType().getCanonicalName() + "-" + sequence.value();

                    if(registry.isBeanNameInUse(seqBeanName))
                    {
                        continue;
                    }

                    BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition(fld.getType());
                    beanBuilder.addConstructorArgValue(sequence.value());

                    log.info("注册序列生产器 Bean: {}", seqBeanName);

                    registry.registerBeanDefinition(seqBeanName, beanBuilder.getBeanDefinition());
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        Iterator<String> iter = beanFactory.getBeanNamesIterator();
        while(iter.hasNext())
        {
            String beanName = iter.next();
            if(!beanFactory.containsBeanDefinition(beanName))
            {
                continue;
            }

            String beanClassName = beanFactory.getBeanDefinition(beanName).getBeanClassName();

            if(!StringUtil.startsWith(beanClassName, BootInfoHolder.getAppPackage()))
            {
                continue;
            }

            Class<?> clz = ClassUtil.parseClass(beanClassName);
            if(clz == null)
            {
                continue;
            }

            // 给 Sequence 依赖对象赋值
            {
                Set<Field> annoFlds = ClassUtil.getAnnotationFields(clz, Sequence.class);

                for(Field fld: annoFlds)
                {
                    Sequence sequence = fld.getAnnotation(Sequence.class);

                    // Sequence Bean 名字
                    String seqBeanName = fld.getType().getCanonicalName() + "-" + sequence.value();
                    Object seqBean = beanFactory.getBean(seqBeanName);

                    try
                    {
                        // 注入
                        fld.set(beanFactory.getBean(beanName), seqBean);
                    }
                    catch(IllegalArgumentException | IllegalAccessException e)
                    {
                        log.error("准备注入序列生产器 Bean 失败: {} => {}[{}], {}", seqBeanName, beanName, fld.getName(), e.getMessage(), e);
                    }

                    log.info("注入序列生产器 Bean: {} => {}[{}]", seqBeanName, beanName, fld.getName());
                }
            }

        }
    }
}
