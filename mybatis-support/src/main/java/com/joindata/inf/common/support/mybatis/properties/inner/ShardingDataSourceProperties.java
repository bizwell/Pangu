package com.joindata.inf.common.support.mybatis.properties.inner;

import java.util.List;

import javax.sql.DataSource;

import com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource;
import com.joindata.inf.common.sterotype.jdbc.sterotype.BaseDataSourceProperties;
import com.joindata.inf.common.sterotype.jdbc.sterotype.SlaveDataSourceProperties;
import com.joindata.inf.common.support.mybatis.support.MasterSlaveDatasourceWrapper;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.JdbcConn;

/**
 * 数据源配置变量类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月1日 上午11:40:23
 */
public class ShardingDataSourceProperties extends BaseDataSourceProperties
{
    private static final Logger log = Logger.get();

    @Override
    public DataSource toDataSource()
    {
        if(ArrayUtil.isEmpty(this.getSlaves()))
        {
            return super.toDataSource();
        }

        JdbcConn connProps = NetworkUtil.parseJdbcConn(this.getUrl());
        List<DataSource> slaves = CollectionUtil.newList();

        for(SlaveDataSourceProperties slaveProps: this.getSlaves())
        {
            slaves.add(slaveProps.toDataSource());
        }

        log.info("数据源 {} 有 {} 个 Slave 节点", connProps.getDbName(), slaves.size());

        MasterSlaveDataSource masterSlaveDataSource = new MasterSlaveDataSource(connProps.getDbName(), super.toDataSource(), slaves);
        return new MasterSlaveDatasourceWrapper(masterSlaveDataSource);
    }
}
