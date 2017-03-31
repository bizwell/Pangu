package com.joindata.inf.common.support.mybatis.bootconfig;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.sterotype.jdbc.support.RoutingDataSource;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.support.mybatis.support.CustomVfs;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ClassUtil;

/**
 * MyBatis 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午10:32:18
 */
@Configuration
public class MyBatisConfig
{
    @Autowired
    private RoutingDataSource dataSource;

    static
    {
        System.setProperty("druid.logType", "log4j2");
    }
    
    @Bean
    public org.apache.ibatis.session.Configuration sqlSessionFactoryConfiguration()
    {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(Utils.autoCamel());
        return configuration;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean()
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setVfs(CustomVfs.class);
        bean.setConfiguration(sqlSessionFactoryConfiguration());
        

        if(!ArrayUtil.isEmpty(Utils.instantPlugins()))
        {
            bean.setPlugins(Utils.instantPlugins());
        }

        return bean;
    }

    /** 实用工具 */
    private static final class Utils
    {
        /**
         * 实例化自定义插件
         */
        public static final Interceptor[] instantPlugins()
        {
            Class<? extends Interceptor>[] clzes = BootInfoHolder.getBootClass().getAnnotation(EnableMyBatis.class).plugins();
            Interceptor interceptors[] = new Interceptor[clzes.length];

            int i = 0;
            for(Class<? extends Interceptor> clz: clzes)
            {
                interceptors[i++] = ClassUtil.newInstance(clz);
            }

            return interceptors;
        }

        /**
         * 是否使用自动驼峰装换
         */
        public static final boolean autoCamel()
        {
            return BootInfoHolder.getBootClass().getAnnotation(EnableMyBatis.class).autoCamel();
        }
    }
}