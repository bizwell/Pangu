package com.joindata.inf.common.support.mybatis.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.joindata.inf.common.sterotype.jdbc.support.RoutingDataSource;
import com.joindata.inf.common.util.log.Logger;

@Configuration
@EnableTransactionManagement
public class TransactionConfig
{
    private static final Logger log = Logger.get();

    /** 大 DataSource，唯一的，暴露给 TX 的 */
    @Autowired
    private RoutingDataSource dataSource;

    /**
     * Spring 事务管理器
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager()
    {
        log.info("启用注解事务管理器");
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
        return manager;
    }

    /**
     * 可以直接注入后使用，用于复杂的事务处理场景
     */
    @Bean
    @Lazy
    public TransactionTemplate transactionTemplate()
    {
        log.info("启用 TransactionTemplate 事务管理器");
        return new TransactionTemplate(dataSourceTransactionManager());
    }
}