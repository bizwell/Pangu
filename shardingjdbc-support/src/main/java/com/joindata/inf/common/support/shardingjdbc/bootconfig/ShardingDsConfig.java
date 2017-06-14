package com.joindata.inf.common.support.shardingjdbc.bootconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.joindata.inf.common.sterotype.jdbc.cst.DatasourceName;
import com.joindata.inf.common.sterotype.jdbc.support.DataSourceRoutingHolder;

@Configuration
public class ShardingDsConfig
{
    @Autowired
    private ShardingRule shardingRule;

    @Autowired
    private DataSourceRoutingHolder holder;

    @Bean
    public void shardingDataSource()
    {
        DataSource shardingDs = ShardingDataSourceFactory.createDataSource(shardingRule);

        holder.addDataSource(DatasourceName.ShardingJDBC, shardingDs);
    }

}
