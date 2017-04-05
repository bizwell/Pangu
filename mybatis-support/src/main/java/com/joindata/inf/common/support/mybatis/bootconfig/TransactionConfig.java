package com.joindata.inf.common.support.mybatis.bootconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.joindata.inf.common.sterotype.jdbc.support.DataSourceRoutingHolder;
import com.joindata.inf.common.sterotype.jdbc.support.DataSourceType;
import com.joindata.inf.common.sterotype.jdbc.support.RoutingDataSource;
import com.joindata.inf.common.support.mybatis.support.properties.DataSourceProperties;
import com.joindata.inf.common.util.log.Logger;

@Configuration
@EnableTransactionManagement
public class TransactionConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private DataSourceProperties properties;

    /** 大 DataSource，唯一的，暴露给 TX 的 */
    @Autowired
    private RoutingDataSource dataSource;

    @Bean
    public DataSource mybatisDatasource()
    {
        DataSource ds = properties.toDataSource();
        DataSourceRoutingHolder.addDataSource(DataSourceType.SINGLE, ds);

        log.info("Druid 数据源连接地址: {}, 用户: {}, 最大活动连接数: {}, 初始化大小: {}", properties.getUrl(), properties.getUsername(), properties.getMaxActive(), properties.getInitialSize());

        return ds;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager()
    {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
        return manager;
    }

    @Bean
    public TransactionTemplate transactionTemplate()
    {
        return new TransactionTemplate(dataSourceTransactionManager());
    }
}