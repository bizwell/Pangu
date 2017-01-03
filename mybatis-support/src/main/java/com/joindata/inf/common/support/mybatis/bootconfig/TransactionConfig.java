package com.joindata.inf.common.support.mybatis.bootconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.joindata.inf.common.support.mybatis.support.properties.DataSourceProperties;
import com.joindata.inf.common.util.log.Logger;

@Configuration
@EnableTransactionManagement
public class TransactionConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private DataSourceProperties properties;

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

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager()
    {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource());
        return manager;
    }

    @Bean
    public TransactionTemplate transactionTemplate()
    {
        return new TransactionTemplate(dataSourceTransactionManager());
    }
}