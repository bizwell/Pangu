package com.joindata.inf.common.support.idgen.bootconfig;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
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
public class IdGenBeanRegistry extends CommonAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor, ApplicationContextAware
{
    private static final long serialVersionUID = 9067044716238627933L;

    private static final Logger log = Logger.get();

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        Map<Class<?>, BeanDefinition> beanDefMap = CollectionUtil.newMap();
        for(String beanName: registry.getBeanDefinitionNames())
        {
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);

            // 只解析应用包里的
            if(!StringUtil.startsWith(beanDef.getBeanClassName(), BootInfoHolder.getAppPackage()))
            {
                continue;
            }

            Class<?> clz = ClassUtil.parseClass(beanDef.getBeanClassName());
            if(clz == null)
            {
                continue;
            }

            beanDefMap.put(clz, beanDef);
        }

        for(Class<?> clz: beanDefMap.keySet())
        {
            Set<Field> annoFlds = ClassUtil.getAnnotationFields(clz, Sequence.class);

            // 注册 Sequence Bean
            {

                for(Field fld: annoFlds)
                {
                    log.info("正在处理: {}", clz.getCanonicalName());

                    Sequence sequence = fld.getAnnotation(Sequence.class);

                    String seqBeanName = fld.getType().getCanonicalName() + "-" + sequence.value();

                    if(registry.isBeanNameInUse(seqBeanName))
                    {
                        continue;
                    }

                    BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition(fld.getType());
                    Class<?> dependencyClass = fld.getType();
                    Set<Field> dependencyFields = ClassUtil.getAnnotationFields(dependencyClass, Resource.class);
                    for(Field field: dependencyFields)
                    {
                        String dependencyBeanName = field.getAnnotation(Resource.class).name();
                        beanBuilder.addDependsOn(dependencyBeanName);
                        beanBuilder.addPropertyReference(field.getName(), dependencyBeanName);
                    }
                    beanBuilder.addPropertyValue("name", sequence.value());
                    log.info("注册序列生产器 Bean: {}", seqBeanName);

                    registry.registerBeanDefinition(seqBeanName, beanBuilder.getBeanDefinition());
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        // DO NOTHING
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {

        // 给 Sequence 依赖对象赋值
        {
            Set<Field> annoFlds = ClassUtil.getAnnotationFields(bean.getClass(), Sequence.class);

            for(Field fld: annoFlds)
            {
                Sequence sequence = fld.getAnnotation(Sequence.class);

                // Sequence Bean 名字
                String seqBeanName = fld.getType().getCanonicalName() + "-" + sequence.value();
                Object seqBean = this.applicationContext.getBean(seqBeanName);

                try
                {
                    // 注入
                    fld.set(bean, seqBean);
                }
                catch(IllegalArgumentException | IllegalAccessException e)
                {
                    log.error("准备注入序列生产器 Bean 失败: {} => {}[{}], {}", seqBeanName, beanName, fld.getName(), e.getMessage(), e);
                }

                log.info("注入序列生产器 Bean: {} => {}[{}]", seqBeanName, beanName, fld.getName());
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
