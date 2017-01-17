package com.joindata.inf.common.support.mybatis.bootconfig;

import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Repository;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 把官方的 MapperScannerRegister 重载了一下，以支持 @EnableMyBatis 注解
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午6:36:47
 */
public class CustomMapperScannerRegistrar extends MapperScannerRegistrar
{
    private static final Logger log = Logger.get();

    private ResourceLoader resourceLoader;

    /*
     * (non-Javadoc)
     * @see org.mybatis.spring.annotation.MapperScannerRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
    {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        // 只扫描 Repository 标记的
        scanner.setAnnotationClass(Repository.class);

        // this check is needed in Spring 3.1
        if(resourceLoader != null)
        {
            scanner.setResourceLoader(resourceLoader);
        }

        log.info("MyBatis 扫描包为: " + ArrayUtil.join(Util.getScanPackage()));

        scanner.registerFilters();
        scanner.doScan(Util.getScanPackage());
    }

    /*
     * (non-Javadoc)
     * @see org.mybatis.spring.annotation.MapperScannerRegistrar#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 一些工具
     * 
     * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
     * @date 2016年12月2日 下午2:13:15
     */
    private static final class Util
    {
        /**
         * 获取 MyBatis 扫描的包<br />
         * <i>将会解析启动类上的 EnableMyBatis 注解，解析过程中，如果注解中写了包名，那就使用注解中的包名，否则使用启动类所在的包名</i>
         * 
         * @return 全部需要扫描的包名
         */
        static final String[] getScanPackage()
        {
            // 获取启动类的扫描包
            Class<?> bootClz = BootInfoHolder.getBootClass();
            EnableMyBatis anno = bootClz.getAnnotation(EnableMyBatis.class);

            if(ArrayUtil.isEmpty(anno.value()))
            {
                return new String[]{bootClz.getPackage().getName()};
            }

            return anno.value();
        }
    }
}