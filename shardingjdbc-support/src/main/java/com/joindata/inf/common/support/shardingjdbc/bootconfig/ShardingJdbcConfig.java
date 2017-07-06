package com.joindata.inf.common.support.shardingjdbc.bootconfig;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
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
import com.joindata.inf.common.support.mybatis.properties.inner.ShardingDataSourceProperties;
import com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long.GroupRangeSuffixChooser;
import com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long.ModShardingTableAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long.ModShardingTableWithMultikeyAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long.RangeGroupSchemeAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long.RangeGroupSchemeMultikeyAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.properties.ShardingJdbcConf;
import com.joindata.inf.common.support.shardingjdbc.properties.inner.ShardingWithGroupByColumnRule;
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

    // {datasourceName}.{tbName}
    private static final String DELIMITER = ".";

    @Bean
    public DataSourceRule dataSourceRule() throws GeneralSecurityException
    {
        DataSourceRule rule = new DataSourceRule(getDataSourceMap());
        log.info("ShardingJDBC 数据源: {}", rule.getDataSourceNames());

        return rule;
    }

    @Bean
    public ShardingRule shardingRule() throws GeneralSecurityException
    {
        ShardingRuleBuilder builder = new ShardingRuleBuilder();
        // 单分表键对应的rule
        Collection<TableRule> tableRulesWithSingleShardingKey = generateTableRulesWithSingleShardingKey();
        // 多分表键对应的rule
        Collection<TableRule> tableRulesWithMultiShardingKey = generateTableRulesWithMultiShardingKey();
        tableRulesWithSingleShardingKey.addAll(tableRulesWithMultiShardingKey);
        builder.tableRules(tableRulesWithSingleShardingKey);
        builder.dataSourceRule(dataSourceRule());

        return builder.build();
    }

    /**
     * 根据配置文件创建表规则
     * 
     * @return 表规则集合
     * @throws GeneralSecurityException
     */
    private Collection<TableRule> generateTableRulesWithSingleShardingKey() throws GeneralSecurityException
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

                if(CollectionUtil.isNullOrEmpty(dsMap))
                {
                    log.fatal("没有设置 {} 的分片数据源，也没有默认数据源。也就是说啥数据源都没有配置，再见！", rule.getSchemaName());
                    System.exit(1);
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
     * 多个分库分表键对应的table rule
     * 
     * @return
     * @throws GeneralSecurityException
     */
    private Collection<TableRule> generateTableRulesWithMultiShardingKey() throws GeneralSecurityException
    {

        Collection<TableRule> rules = CollectionUtil.newHashSet();
        if(!ArrayUtil.isEmpty(conf.getShardingWithGroupByColumnRule()))
        {
            for(ShardingWithGroupByColumnRule rule: conf.getShardingWithGroupByColumnRule())
            {
                String logicTable = rule.getLogicTable();
                TableRuleBuilder builder = new TableRuleBuilder(logicTable);
                List<ShardingWithGroupRule> shardingColumnRules = rule.getShardingColumnRules();
                // 设置物理表
                List<String> shardingColumns = new ArrayList<>();
                // 挑选适当的数据源
                Map<String, DataSource> dsMap = CollectionUtil.newMap();
                Map<String, ShardingWithGroupRule> shardingColumnRuleMap = CollectionUtil.newMap();
                Map<String, GroupRangeSuffixChooser> dbChooserFactory = CollectionUtil.newMap();
                String defaultDs = null;
                List<String> tables = CollectionUtil.newList();
                for(ShardingWithGroupRule shardingColumnRule: shardingColumnRules)
                {
                    String shardingColumn = shardingColumnRule.getShardingKey();
                    shardingColumns.add(shardingColumn);
                    shardingColumnRuleMap.put(shardingColumn, shardingColumnRule);
                    Set<String> dbNames = getDataSourceMap().keySet();
                    String schemaName = shardingColumnRule.getSchemaName();
                    String tableName = shardingColumnRule.getTableName();
                    builder.actualTables(tables);

                    for(String dbName: dbNames)
                    {
                        // 分组库
                        if(ValidateUtil.isPureNumberText(StringUtil.replaceAll(dbName.toUpperCase(), schemaName.toUpperCase() + "_", "")))
                        {
                            dsMap.put(dbName, getDataSourceMap().get(dbName));
                            for(int i = 0; i < shardingColumnRule.getShardingCount(); i++)
                            {
                                tables.add(dbName + DELIMITER + tableName + "_" + i);
                            }
                        }
                        // 默认库
                        if(StringUtil.isEqualsIgnoreCase(dbName, shardingColumnRule.getSchemaName()))
                        {
                            defaultDs = dbName;
                            dsMap.put(defaultDs, getDataSourceMap().get(dbName));
                        }

                    }

                    // 针对每个分库分表键, 构造所属的db选择器
                    GroupRangeSuffixChooser dbChooser = new GroupRangeSuffixChooser(defaultDs, shardingColumnRule.getGroupVolum(), shardingColumnRule.getKeyEnob());
                    dbChooserFactory.put(shardingColumn, dbChooser);
                }

                builder.actualTables(tables);
                // 设置表分片策略
                builder.tableShardingStrategy(new TableShardingStrategy(shardingColumns, new ModShardingTableWithMultikeyAlgorithm(shardingColumnRuleMap)));

                // 设置物理库
                builder.dataSourceRule(new DataSourceRule(dsMap, defaultDs));

                // 设置数据库分组策略
                builder.databaseShardingStrategy(new RangeGroupSchemeMultikeyAlgorithm(shardingColumnRuleMap, dbChooserFactory).toShardingStrategy(shardingColumns));

                rules.add(builder.build());

                log.info("已创建规则, 类型: {}, 表名: {}, 实际表: {}, 实际库: {}", "按库分组，按表取模", rule.getLogicTable(), tables, dsMap.keySet());
            }
        }
        return rules;
    }

    /**
     * 获取数据源集合
     * 
     * @return 数据源 Map<数据库名, 数据源实例>
     * @throws GeneralSecurityException
     */
    private Map<String, DataSource> getDataSourceMap() throws GeneralSecurityException
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