package com.joindata.inf.common.support.shardingjdbc.properties.inner;

import com.joindata.inf.common.util.basic.JsonUtil;

import lombok.Data;

/**
 * 用库分组、表取模的规则
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 29, 2017 2:13:16 PM
 */
@Data
public class ShardingWithGroupRule
{
    /** 数据库名 */
    private String schemaName;

    /** 逻辑表名 */
    private String tableName;

    /**
     * 分组容量，表示在 <strong>分组键</strong> 增长到 <strong>每多少</strong> 的时候需要数据库分组递增
     */
    private int groupVolum;

    /**
     * 分表容量，表示一组内多少张表，用于取模分片
     */
    private int shardingCount;

    /** 分表键 */
    private String shardingKey;

    /** 分表键有效位 */
    private byte keyEnob;

    public String getSchemaName()
    {
        return schemaName;
    }

    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public int getGroupVolum()
    {
        return groupVolum;
    }

    public void setGroupVolum(int groupVolum)
    {
        this.groupVolum = groupVolum;
    }

    public int getShardingCount()
    {
        return shardingCount;
    }

    public void setShardingCount(int shardingCount)
    {
        this.shardingCount = shardingCount;
    }

    public String getShardingKey()
    {
        return shardingKey;
    }

    public void setShardingKey(String shardingKey)
    {
        this.shardingKey = shardingKey;
    }

    public byte getKeyEnob()
    {
        return keyEnob;
    }

    public void setKeyEnob(byte keyEnob)
    {
        this.keyEnob = keyEnob;
    }

    public static void main(String[] args)
    {
        System.out.println(JsonUtil.toJSON(new ShardingWithGroupRule()));
    }

}
