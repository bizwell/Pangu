package com.joindata.inf.common.support.mybatis.bootconfig;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.joindata.inf.common.support.mybatis.support.properties.DataSourceProperties;
import com.joindata.inf.common.util.log.Logger;

/**
 * MyBatis 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午10:32:18
 */
@Configuration
public class MyBatisConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private DataSourceProperties properties;

    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean()
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());

        return bean;
    }

    @Bean
    public DataSource dataSource()
    {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(properties.getUrl());
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        ds.setInitialSize(properties.getInitialSize());
        ds.setMinIdle(properties.getMinIdle());
        ds.setMaxActive(properties.getMaxActive());
        ds.setMaxWait(properties.getMaxWait());
        ds.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        ds.setValidationQuery(properties.getValidationQuery());
        ds.setTestWhileIdle(properties.isTestWhileIdle());
        ds.setTestOnBorrow(properties.isTestOnBorrow());
        ds.setTestOnReturn(properties.isTestOnReturn());

        log.info("Druid 数据源连接地址: {}, 用户: {}, 最大活动连接数: {}, 初始化大小: {}", properties.getUrl(), properties.getUsername(), properties.getMaxActive(), properties.getInitialSize());

        return ds;
    }
}