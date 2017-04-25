package com.joindata.inf.common.sterotype.jdbc.bootconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.sterotype.jdbc.support.DataSourceRoutingHolder;
import com.joindata.inf.common.sterotype.jdbc.support.RoutingDataSource;

@Configuration
public class DataSourceConfig
{
    static
    {
        System.setProperty("druid.logType", "log4j2");
    }

    @Bean
    public RoutingDataSource dataSource()
    {
        RoutingDataSource dataSource = new RoutingDataSource();
        dataSource.setTargetDataSources(DataSourceRoutingHolder.getDataSourceMap());
        return dataSource;
    }
}
