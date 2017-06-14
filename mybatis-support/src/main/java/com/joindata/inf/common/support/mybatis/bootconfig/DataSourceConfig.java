package com.joindata.inf.common.support.mybatis.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.sterotype.jdbc.support.DataSourceRoutingHolder;
import com.joindata.inf.common.sterotype.jdbc.support.RoutingDataSource;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.support.mybatis.properties.DatasourceConf;
import com.joindata.inf.common.support.mybatis.support.properties.DataSourceProperties;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

@Configuration
public class DataSourceConfig
{
    private static final Logger log = Logger.get();

    static
    {
        System.setProperty("druid.logType", "log4j2");
    }

    @Autowired
    private DataSourceProperties properties;

    @Autowired
    private DatasourceConf conf;

    /**
     * 数据源路由键管理对象
     */
    @Bean
    public DataSourceRoutingHolder holder()
    {
        log.info("默认数据源是: {}", Utils.defaultDatasource());
        DataSourceRoutingHolder holder = new DataSourceRoutingHolder(routingDataSource());
        holder.setDefaultDatasource(Utils.defaultDatasource());

        // 数据源配置
        if(conf.isExists())
        {
            log.info("牛逼！就应该用这个配置文件");
            holder.addDataSource(conf.toDatasourceMap());
            log.info("一共配置了这几个数据源: {}", conf.toString());
        }
        // 如果新的不存在，就用旧的
        else
        {
            log.warn("jdbc.properties 该文件已不建议使用，从 pangu 1.2.0 开始，请将数据源配置逐步迁移到 datasource.json 中，该配置支持多种数据源策略");

            DruidDataSource ds = new DruidDataSource();
            ds.setName(properties.getName());
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

            if(StringUtil.isNullOrEmpty(properties.getName()))
            {
                properties.setName("");
            }

            holder.addDataSource(properties.getName(), ds);

            log.info("Druid 数据源连接地址: {}, 用户: {}, 最大活动连接数: {}, 初始化大小: {}", properties.getUrl(), properties.getUsername(), properties.getMaxActive(), properties.getInitialSize());
        }

        return holder;
    }

    /**
     * 动态数据源
     */
    @Bean
    public RoutingDataSource routingDataSource()
    {
        RoutingDataSource ds = new RoutingDataSource();
        ds.setTargetDataSources(DataSourceRoutingHolder.getDataSourceMap());
        return ds;
    }

    /** 实用工具 */
    private static final class Utils
    {
        /**
         * 获取默认数据源
         * 
         * @return 数据源名
         */
        public static final String defaultDatasource()
        {
            return BootInfoHolder.getBootClass().getAnnotation(EnableMyBatis.class).defaultDatasource();
        }
    }

}
