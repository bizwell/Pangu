package com.joindata.inf.common.support.shardingjdbc.bootconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.joindata.inf.common.sterotype.jdbc.support.DataSourceRoutingHolder;
import com.joindata.inf.common.sterotype.jdbc.support.DataSourceType;

@Configuration
public class ShardingDsConfig
{
    @Autowired
    private ShardingRule shardingRule;

    @Bean
    public DataSource shardingDataSource()
    {
        DataSource shardingDs = ShardingDataSourceFactory.createDataSource(shardingRule);

        DataSourceRoutingHolder.addDataSource(DataSourceType.SHARDINGJDBC, shardingDs);

        return shardingDs;
    }
}
