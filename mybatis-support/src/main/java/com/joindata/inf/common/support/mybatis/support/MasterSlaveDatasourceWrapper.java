package com.joindata.inf.common.support.mybatis.support;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.dangdang.ddframe.rdb.sharding.jdbc.MasterSlaveDataSource;
import com.dangdang.ddframe.rdb.sharding.jdbc.adapter.AbstractDataSourceAdapter;
import com.dangdang.ddframe.rdb.sharding.parser.result.router.SQLStatementType;
import com.joindata.inf.common.util.log.Logger;

public class MasterSlaveDatasourceWrapper extends AbstractDataSourceAdapter
{
    private static final Logger log = Logger.get();

    private MasterSlaveDataSource masterSlaveDataSource;

    public MasterSlaveDatasourceWrapper(MasterSlaveDataSource masterSlaveDataSource)
    {
        this.masterSlaveDataSource = masterSlaveDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        SQLStatementType sqlStatementType = SQLStatementTypeHolder.get();
        log.info("sql 类型: {}", sqlStatementType);
        DataSource dataSource = masterSlaveDataSource.getDataSource(sqlStatementType);
        return dataSource.getConnection();
    }

}
