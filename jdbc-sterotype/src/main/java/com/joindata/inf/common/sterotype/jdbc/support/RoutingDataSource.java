package com.joindata.inf.common.sterotype.jdbc.support;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源选择器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 27, 2017 2:54:03 PM
 */
public class RoutingDataSource extends AbstractRoutingDataSource
{
    @Override
    protected Object determineCurrentLookupKey()
    {
        return DataSourceRoutingHolder.getRoutingKey();
    }

}
