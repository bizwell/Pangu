package com.joindata.inf.common.support.shardingjdbc.bootconfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule.ShardingRuleBuilder;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule.TableRuleBuilder;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long.ModShardingTableAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long.RangeGroupSchemeAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.properties.ShardingJdbcConf;
import com.joindata.inf.common.support.shardingjdbc.properties.inner.ShardingDataSourceProperties;
import com.joindata.inf.common.support.shardingjdbc.properties.inner.ShardingWithGroupRule;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.basic.ValidateUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.JdbcConn;

/**
 * ShardingJDBC 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 20, 2017 10:35:18 AM
 */
@Configuration
public class ShardingJdbcConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private ShardingJdbcConf conf;

    private Map<String, DataSource> DS_MAP;

    // @PostConstruct
    // public void init()
    // {
    // getDataSourceMap();
    // }

    @Bean
    public DataSourceRule dataSourceRule()
    {
        DataSourceRule rule = new DataSourceRule(getDataSourceMap());

        log.info("ShardingJDBC 数据源: {}", rule.getDataSourceNames());

        return rule;
    }

    @Bean
    public ShardingRule shardingRule()
    {
        ShardingRuleBuilder builder = new ShardingRuleBuilder();
        builder.tableRules(generateTableRules());
        builder.dataSourceRule(dataSourceRule());

        return builder.build();
    }

    /**
     * 根据配置文件创建表规则
     * 
     * @return 表规则集合
     */
    private Collection<TableRule> generateTableRules()
    {
        Collection<TableRule> rules = CollectionUtil.newHashSet();

        // 按库分组、按表取模规则
        if(!ArrayUtil.isEmpty(conf.getShardingWithGroupRule()))
        {
            for(ShardingWithGroupRule rule: conf.getShardingWithGroupRule())
            {
                TableRuleBuilder builder = new TableRuleBuilder(rule.getTableName());

                // 设置物理表
                List<String> tables = CollectionUtil.newList();
                for(int i = 0; i < rule.getShardingCount(); i++)
                {
                    tables.add(rule.getTableName() + "_" + i);
                }
                builder.actualTables(tables);

                // 设置表分片策略
                builder.tableShardingStrategy(new TableShardingStrategy(rule.getShardingKey(), new ModShardingTableAlgorithm()));

                // 挑选适当的数据源
                Map<String, DataSource> dsMap = CollectionUtil.newMap();
                Set<String> dbNames = getDataSourceMap().keySet();
                String defaultDs = null;
                for(String dbName: dbNames)
                {
                    // 分组库
                    if(ValidateUtil.isPureNumberText(StringUtil.replaceAll(dbName.toUpperCase(), rule.getSchemaName().toUpperCase() + "_", "")))
                    {
                        dsMap.put(dbName, getDataSourceMap().get(dbName));
                    }
                    // 默认库
                    if(StringUtil.isEqualsIgnoreCase(dbName, rule.getSchemaName()))
                    {
                        defaultDs = dbName;
                        dsMap.put(defaultDs, getDataSourceMap().get(dbName));
                    }
                }

                // 设置物理库
                builder.dataSourceRule(new DataSourceRule(dsMap, defaultDs));

                // 设置数据库分组策略
                builder.databaseShardingStrategy(new RangeGroupSchemeAlgorithm(defaultDs, rule.getGroupVolum(), rule.getKeyEnob()).toShardingStrategy(rule.getShardingKey()));

                rules.add(builder.build());

                log.info("已创建规则, 类型: {}, 表名: {}, 实际表: {}, 分组数据库: {}, 实际库: {}", "按库分组，按表取模", rule.getTableName(), tables, rule.getSchemaName(), dsMap.keySet());
            }
        }

        return rules;
    }

    /**
     * 获取数据源集合
     * 
     * @return 数据源 Map<数据库名, 数据源实例>
     */
    private Map<String, DataSource> getDataSourceMap()
    {
        if(DS_MAP == null)
        {
            DS_MAP = CollectionUtil.newMap();

            log.info("读取数据源配置 - 开始");
            for(ShardingDataSourceProperties properties: conf.getDataSources())
            {
                JdbcConn connProps = NetworkUtil.parseJdbcConn(properties.getUrl());
                DS_MAP.put(connProps.getDbName(), properties.toDataSource());
                log.info("Druid 数据源连接地址: {}, 名称: {}, 用户: {}, 最大活动连接数: {}, 初始化大小: {}", connProps.getDbName(), properties.getUrl(), properties.getUsername(), properties.getMaxActive(), properties.getInitialSize());
            }

            log.info("读取数据源配置 - 结束");
        }

        return DS_MAP;
    }

}