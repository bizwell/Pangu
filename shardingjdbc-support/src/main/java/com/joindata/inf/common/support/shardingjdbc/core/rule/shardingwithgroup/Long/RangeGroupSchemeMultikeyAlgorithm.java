package com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.MultipleKeysDatabaseShardingAlgorithm;
import com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long.GroupRangeSuffixChooser;
import com.joindata.inf.common.support.shardingjdbc.properties.inner.ShardingColumnRule;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

import lombok.AllArgsConstructor;

/**
 * 多个分库分表键算法
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月31日
 */
@AllArgsConstructor
public class RangeGroupSchemeMultikeyAlgorithm implements MultipleKeysDatabaseShardingAlgorithm
{
    private static final Logger log = Logger.get();

    private Map<String, ShardingColumnRule> shardingColumnRuleMap;
    
    private Map<String, GroupRangeSuffixChooser> dbChooserFactory;
    
	@Override
	public Collection<String> doSharding(Collection<String> availableTargetNames,
			Collection<ShardingValue<?>> shardingValues) {
	    List<String> dbNames = new ArrayList<>();
		for (ShardingValue<?> shardingValue : shardingValues) {
		    String shardingColumnName = shardingValue.getColumnName();
		    ShardingColumnRule shardingColumnRule = shardingColumnRuleMap.get(shardingValue.getColumnName());
		    if (shardingColumnRule == null) {
		        log.warn("分库分表键:{}没有配置对应的rule!", shardingValue.getColumnName());
		        continue;
		    }
		    String schema = shardingColumnRule.getSchemaName();
		    //根据schema的值过滤取得该分表键对应的数据库的列表
		    Collection<String> dbNameList = availableTargetNames.stream().filter(p->StringUtil.substringBeforeLast(p, "_").equalsIgnoreCase(schema)).collect(Collectors.toList());
			 // 直接把传入的值取模
	        Long value = (Long) shardingValue.getValue();
	        GroupRangeSuffixChooser dbChooser = dbChooserFactory.get(shardingColumnName);
	        if (dbChooser == null) {
	            log.warn("分库分表键:{}没有对应的rule!", shardingValue.getColumnName());
                continue;
	        }
	        String targeDb = dbChooser.choose(dbNameList, value);
	        dbNames.add(targeDb);
		}
		
		return dbNames;
	}
	
	  /**
     * 以当前算法生成数据库分片策略
     * 
     * @param 分组键
     * @return 数据库分片策略
     */
    public DatabaseShardingStrategy toShardingStrategy(List<String> shardingKeys)
    {
        return new DatabaseShardingStrategy(shardingKeys, this);
    }
}