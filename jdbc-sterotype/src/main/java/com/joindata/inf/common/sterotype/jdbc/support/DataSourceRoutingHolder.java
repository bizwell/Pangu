package com.joindata.inf.common.sterotype.jdbc.support;

import java.util.Map;

import javax.sql.DataSource;

import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 数据源类型持有者
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 27, 2017 2:55:44 PM
 */
public class DataSourceRoutingHolder
{
    private static Map<Object, Object> DS_MAP = CollectionUtil.newMap();

    /**
     * 读取数据源 Map
     * 
     * @return 数据源 Map
     */
    public static Map<Object, Object> getDataSourceMap()
    {
        return DS_MAP;
    }

    /**
     * 添加数据源
     * 
     * @param type 数据源类型
     * @param ds 数据源
     */
    public static void addDataSource(DataSourceType type, DataSource ds)
    {
        DS_MAP.put(type, ds);

        SpringContextHolder.getBean(RoutingDataSource.class).afterPropertiesSet();
    }

    /**
     * 获取 DataSource 的类型<br />
     * <i>由于不同的组件会设置不同的类型给 DS_TYPE，这里会按照优先级来挑选合适的数据类型</i>
     * 
     * @return 应该选择的数据源类型
     */
    public static DataSourceType getType()
    {
        if(DS_MAP.containsKey(DataSourceType.SHARDINGJDBC))
        {
            return DataSourceType.SHARDINGJDBC;
        }
        else
        {
            return DataSourceType.SINGLE;
        }
    }
}