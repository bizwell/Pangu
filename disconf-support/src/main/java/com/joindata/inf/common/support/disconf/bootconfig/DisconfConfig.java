package com.joindata.inf.common.support.disconf.bootconfig;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * Disconf 配置类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月22日 上午11:36:52
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DisconfConfig
{
    private static final Logger log = Logger.get();

    /** 扫描的包名 */
    private String scanPackage;

    /**
     * Disconf 管理类
     */
    @Bean(destroyMethod = "destroy")
    public DisconfMgrBean disconfMgrBean()
    {
        DisconfMgrBean bean = new DisconfMgrBean();

        if(StringUtil.isNullOrEmpty(Util.getScanPackage()))
        {
            bean.setScanPackage(scanPackage);
            log.info("Disconf 扫描包: {}", scanPackage);
        }
        else
        {
            bean.setScanPackage(Util.getScanPackage());
            log.info("Disconf 扫描包: {}", Util.getScanPackage());
        }

        return bean;
    }

    /**
     * 傻逼玩意
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DisconfMgrBeanSecond disconfMgrBeanSecond()
    {
        return new DisconfMgrBeanSecond();
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
         * 获取 Disconf 扫描的包<br />
         * <i>将会解析启动类和配置集线器上的 EnableDisconf 注解，解析过程中，如果注解中写了包名，那就使用注解中的包名，否则使用启动类所在的包名</i>
         * 
         * @return 全部需要扫描的包名
         */
        static final String getScanPackage()
        {
            Set<String> scanPackages = CollectionUtil.newHashSet();

            // 获取配置集线器的扫描包
            if(!CollectionUtil.isNullOrEmpty(BootInfoHolder.getConfigHubClasses()))
            {
                for(Class<?> clz: BootInfoHolder.getConfigHubClasses())
                {
                    EnableDisconf anno = clz.getAnnotation(EnableDisconf.class);

                    if(anno == null)
                    {
                        continue;
                    }

                    if(StringUtil.isBlank(anno.value()))
                    {
                        scanPackages.add(clz.getPackage().getName());
                    }
                    else
                    {
                        scanPackages.add(anno.value());
                    }
                }
            }

            // 获取启动类的扫描包
            {
                Class<?> bootClz = BootInfoHolder.getBootClass();

                // 有可能不用自己启动器，这部分可以忽略掉
                if(bootClz != null)
                {
                    EnableDisconf anno = bootClz.getAnnotation(EnableDisconf.class);

                    if(anno != null)
                    {

                        if(anno == null || StringUtil.isBlank(anno.value()))
                        {
                            scanPackages.add(bootClz.getPackage().getName());
                        }
                        else
                        {
                            scanPackages.add(anno.value());
                        }
                    }
                }
            }

            String pkgs[] = CollectionUtil.toArray(scanPackages);

            return ArrayUtil.join(pkgs);
        }
    }
}
