package com.joindata.inf.common.support.redis.component.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.joindata.inf.common.support.redis.component.RedisClient;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;

import redis.clients.jedis.Jedis;

/**
 * 单机版本实现
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 24, 2017 3:43:13 PM
 */
public class SingleRedisClient implements RedisClient
{
    /** Jedis 对象 */
    private Jedis jedis;

    public SingleRedisClient(Jedis jedis)
    {
        this.jedis = jedis;
    }

    /**
     * 获取Jedis 对象
     * 
     * @return Jedis 对象
     */
    public Jedis getJedis()
    {
        return jedis;
    }

    /**
     * 设置字符串
     * 
     * @param key 键
     * @param value 值
     */
    public void put(String key, String value)
    {
        jedis.set(key, value);
    }

    /**
     * 设置会超时的字符串
     * 
     * @param key 键
     * @param seconds 超时秒数
     * @param value 值
     */
    public void putWithExpire(String key, int seconds, String value)
    {
        jedis.setex(key, seconds, value);
    }

    /**
     * 获取字符串
     * 
     * @param key 键
     */
    public String getString(String key)
    {
        String ret = jedis.get(key);

        return ret;
    }

    /**
     * 删除数据
     * 
     * @param key 键
     */
    public void delete(String key)
    {
        jedis.del(key);
        jedis.del(StringUtil.toBytes(key));
    }

    /**
     * 存储对象
     * 
     * @param key 键
     * @param obj 可序列化对象
     */
    public void put(String key, Serializable obj)
    {
        jedis.set(StringUtil.toBytes(key), BeanUtil.serializeObject(obj));
    }

    /**
     * 获取对象
     * 
     * @param key 键
     * @param clz 对象的 Class
     * @return 指定对象实例
     */
    public <T extends Serializable> T get(String key, Class<T> clz)
    {

        byte[] objByte = jedis.get(StringUtil.toBytes(key));

        if(objByte == null)
        {
            return null;
        }

        return BeanUtil.deserializeObject(objByte, clz);
    }

    /**
     * 存储会超时的对象
     * 
     * @param key 键
     * @param seconds 多少秒后超时
     * @param obj 可序列化对象
     */
    public void putWithExpire(String key, int seconds, Serializable obj)
    {
        jedis.setex(StringUtil.toBytes(key), seconds, BeanUtil.serializeObject(obj));
    }

    /**
     * 存储字符串序列（从前插入）<br />
     * <i>values 参数将倒序遍历</i>
     * 
     * @param key 键
     * @param List<String> 字符串列表
     */
    public void prependToList(String key, List<String> values)
    {

        if(StringUtil.isBlank(key) || CollectionUtil.isNullOrEmpty(values))
        {
            return;
        }

        CollectionUtil.reverse(values);

        for(String value: values)
        {
            jedis.rpush(key, value);
        }

    }

    /**
     * 存储字符串序列（从前插入）<br />
     * <i>values 参数将倒序遍历</i>
     * 
     * @param key 键
     * @param values 字符串数组
     */
    public void prependToList(String key, String... values)
    {

        if(StringUtil.isBlank(key) || values == null)
        {
            return;
        }

        int size = values.length;
        for(int i = size - 1; i >= 0; i--)
        {
            jedis.rpush(key, values[i]);
        }

    }

    /**
     * 存储字符串序列（后入式）<br />
     * <i>values 参数将顺序遍历</i>
     * 
     * @param key 键
     * @param values 字符串列表
     */
    public void appendToList(String key, List<String> values)
    {

        if(StringUtil.isBlank(key) || CollectionUtil.isNullOrEmpty(values))
        {
            return;
        }

        for(String value: values)
        {
            jedis.rpush(key, value);
        }

    }

    /**
     * 存储字符串序列（后入式）<br />
     * <i>values 参数将顺序遍历</i>
     * 
     * @param key 键
     * @param values 字符串数组
     */
    public void appendToList(String key, String... values)
    {

        if(StringUtil.isBlank(key) || values == null)
        {
            return;
        }

        for(String value: values)
        {
            jedis.rpush(key, value);
        }

    }

    /**
     * 获取列表，指定起始结束位置
     * 
     * @param key 键
     * @param start 起始，从 0 开始
     * @param end 结束，如果越界将取最大长度
     * @return 字符串数组
     */
    public List<String> getList(String key, int start, int end)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }
        List<String> ret = jedis.lrange(key, start, end);

        return ret;
    }

    /**
     * 获取整个列表
     * 
     * @param key 键
     * @return 字符串数组
     */
    public List<String> getList(String key)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }
        List<String> ret = jedis.lrange(key, 0, -1);

        return ret;
    }

    /**
     * 获取列表，从指定位置截取至最后
     * 
     * @param key 键
     * @param start 起始，从 0 开始
     * @return 字符串数组
     */
    public List<String> getSubList(String key, int start)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }

        List<String> ret = jedis.lrange(key, start, -1);

        return ret;
    }

    /**
     * 获取列表长度
     * 
     * @param key 键
     * @return 列表长度
     */
    public long getListSize(String key)
    {

        long ret = jedis.llen(key);

        return ret;
    }

    /**
     * 存储对象序列（从前插入）<br />
     * <i>values 参数将倒序遍历</i>
     * 
     * @param key 键
     * @param values 对象列表
     */
    public void prependObjectToList(String key, List<? extends Serializable> values)
    {
        if(StringUtil.isBlank(key) || CollectionUtil.isNullOrEmpty(values))
        {
            return;
        }

        CollectionUtil.reverse(values);

        byte keyBytes[] = StringUtil.toBytes(key);

        for(Serializable value: values)
        {
            jedis.rpush(keyBytes, BeanUtil.serializeObject(value));
        }
    }

    /**
     * 存储对象序列（从前插入）<br />
     * <i>values 参数将倒序遍历</i>
     * 
     * @param key 键
     * @param values 对象数组
     */
    public void prependObjectToList(String key, Serializable... values)
    {
        if(StringUtil.isBlank(key) || values == null)
        {
            return;
        }

        byte keyBytes[] = StringUtil.toBytes(key);

        int size = values.length;
        for(int i = size - 1; i >= 0; i--)
        {
            jedis.rpush(keyBytes, BeanUtil.serializeObject(values[i]));
        }

    }

    /**
     * 存储对象序列（后入式）<br />
     * <i>values 参数将顺序遍历</i>
     * 
     * @param key 键
     * @param values 对象列表
     */
    public void appendObjectToList(String key, List<? extends Serializable> values)
    {

        if(StringUtil.isBlank(key) || CollectionUtil.isNullOrEmpty(values))
        {
            return;
        }

        byte keyBytes[] = StringUtil.toBytes(key);

        for(Serializable value: values)
        {
            jedis.rpush(keyBytes, BeanUtil.serializeObject(value));
        }

    }

    /**
     * 存储对象序列（后入式）<br />
     * <i>values 参数将顺序遍历</i>
     * 
     * @param key 键
     * @param values 对象数组
     */
    public void appendObjectToList(String key, Serializable... values)
    {

        if(StringUtil.isBlank(key) || values == null)
        {
            return;
        }

        byte keyBytes[] = StringUtil.toBytes(key);

        for(Serializable value: values)
        {
            jedis.rpush(keyBytes, BeanUtil.serializeObject(value));
        }

    }

    /**
     * 获取列表，指定起始结束位置
     * 
     * @param key 键
     * @param start 起始，从 0 开始
     * @param end 结束，如果越界将取最大长度
     * @param clz 对象的 Class
     * @return 对象列表
     */
    public <T extends Serializable> List<T> getObjectList(String key, int start, int end, Class<T> clz)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }

        List<byte[]> byteList = jedis.lrange(StringUtil.toBytes(key), start, end);

        List<T> list = CollectionUtil.newList();
        if(byteList != null)
        {
            for(byte[] bs: byteList)
            {
                if(bs == null)
                {
                    return null;
                }

                list.add(BeanUtil.deserializeObject(bs, clz));
            }
        }

        return list;
    }

    /**
     * 获取整个列表
     * 
     * @param key 键
     * @param clz 对象的 Class
     * @return 对象列表
     */
    public <T extends Serializable> List<T> getObjectList(String key, Class<T> clz)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }

        List<byte[]> byteList = jedis.lrange(StringUtil.toBytes(key), 0, -1);

        List<T> list = CollectionUtil.newList();
        if(byteList != null)
        {
            for(byte[] bs: byteList)
            {
                if(bs == null)
                {
                    return null;
                }

                list.add(BeanUtil.deserializeObject(bs, clz));
            }
        }

        return list;
    }

    /**
     * 获取列表，从指定位置截取至最后
     * 
     * @param key 键
     * @param start 起始，从 0 开始
     * @return 对象数组
     */
    public <T extends Serializable> List<T> getObjectSubList(String key, int start, Class<T> clz)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }
        List<byte[]> byteList = jedis.lrange(StringUtil.toBytes(key), start, -1);

        List<T> list = CollectionUtil.newList();
        if(byteList != null)
        {
            for(byte[] bs: byteList)
            {
                if(bs == null)
                {
                    return null;
                }
                list.add(BeanUtil.deserializeObject(bs, clz));
            }
        }

        return list;
    }

    /**
     * 存储字符串 Map
     * 
     * @param key 键
     * @param map Map<String, String> 对象
     */
    public void putMap(String key, Map<String, String> map)
    {

        if(StringUtil.isBlank(key) || map == null)
        {
            return;
        }

        jedis.hmset(key, map);

    }

    /**
     * 获取 Map 值
     * 
     * @param key 键
     * @param entries Map 的 key
     * @return 值
     */
    public String getMapValue(String key, String entry)
    {

        if(StringUtil.isBlank(key) || StringUtil.isBlank(entry))
        {
            return null;
        }

        String ret = jedis.hget(key, entry);

        return ret;
    }

    /**
     * 获取 Map 值列表
     * 
     * @param key 键
     * @param entries Map 的 key 列表
     * @return 值列表
     */
    public List<String> getMapValues(String key, String... entries)
    {

        if(StringUtil.isBlank(key) || entries == null)
        {
            return null;
        }

        List<String> ret = jedis.hmget(key, entries);

        return ret;
    }

    /**
     * 获取整个字符串 Map
     * 
     * @param key 键
     * @return 整个字符串 Map
     */
    public Map<String, String> getMap(String key)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }

        Map<String, String> ret = jedis.hgetAll(key);

        return ret;
    }

    /**
     * 删除 Map 中的元素
     * 
     * @param key 键
     * @param entries Map 的 key 列表
     */
    public void deleteMapItem(String key, String... entries)
    {

        if(StringUtil.isBlank(key) || entries == null)
        {
            return;
        }

        jedis.hdel(key, entries);

        byte[][] bytes = new byte[entries.length][];
        for(int i = 0; i < entries.length; i++)
        {
            bytes[i] = StringUtil.toBytes(entries[i]);
        }
        jedis.hdel(StringUtil.toBytes(key), bytes);

    }

    /**
     * 存储对象 Map，Map 类型为 Map<String, ? extends Serializable>
     * 
     * @param key 键
     * @param map Map<String, ? extends Serializable> 对象
     */
    public void putObjectMap(String key, Map<String, ? extends Serializable> map)
    {

        if(StringUtil.isBlank(key) || map == null)
        {
            return;
        }

        Map<byte[], byte[]> bytesMap = CollectionUtil.newMap();

        Iterator<String> iter = CollectionUtil.iteratorMapKey(map);
        while(iter.hasNext())
        {
            String name = iter.next();
            bytesMap.put(name.getBytes(), BeanUtil.serializeObject(map.get(name)));
        }

        jedis.hmset(StringUtil.toBytes(key), bytesMap);

    }

    /**
     * 获取对象 Map 值列表
     * 
     * @param key 键
     * @param clz 值的 Class
     * @param entries Map 的 key 列表
     * @return 值列表
     */
    public <T extends Serializable> List<T> getObjectMapValues(String key, Class<T> clz, String... entries)
    {

        if(StringUtil.isBlank(key) || entries == null)
        {
            return null;
        }

        byte[][] entriesBytes = new byte[entries.length][];
        for(int i = 0; i < entries.length; i++)
        {
            entriesBytes[i] = entries[i].getBytes();
        }

        List<byte[]> bytesList = jedis.hmget(StringUtil.toBytes(key), entriesBytes);
        List<T> list = CollectionUtil.newList();

        for(byte[] bytes: bytesList)
        {
            if(bytes == null)
            {
                list.add(null);
                continue;
            }

            list.add(BeanUtil.deserializeObject(bytes, clz));
        }

        return list;
    }

    /**
     * 获取整个对象 Map
     * 
     * @param key 键
     * @param clz 值的 Class
     * @return 整个对象 Map
     */
    public <T extends Serializable> Map<String, T> getObjectMap(String key, Class<T> clz)
    {

        if(StringUtil.isBlank(key) || clz == null)
        {
            return null;
        }

        Map<byte[], byte[]> bytesMap = jedis.hgetAll(StringUtil.toBytes(key));
        Map<String, T> map = CollectionUtil.newMap();

        Iterator<byte[]> iter = CollectionUtil.iteratorMapKey(bytesMap);
        if(iter == null)
        {
            return map;
        }

        while(iter.hasNext())
        {
            byte[] entryBytes = iter.next();
            byte[] valueBytes = bytesMap.get(entryBytes);
            if(valueBytes == null)
            {
                map.put(key, null);
                continue;
            }

            map.put(StringUtil.toString(entryBytes), BeanUtil.deserializeObject(valueBytes, clz));
        }

        return map;
    }

    /**
     * 获取对象 Map 值
     * 
     * @param key 键
     * @param clz 值的 Class
     * @param entries Map 的 key
     * @return 值
     */
    public <T extends Serializable> T getObjectMapValue(String key, Class<T> clz, String entry)
    {

        if(StringUtil.isBlank(key) || StringUtil.isBlank(entry))
        {
            return null;
        }

        byte bytes[] = jedis.hget(StringUtil.toBytes(key), entry.getBytes());

        if(bytes == null)
        {
            return null;
        }

        return BeanUtil.deserializeObject(bytes, clz);
    }

    /**
     * 序列值 +1
     * 
     * @param key 键
     * @return +1 后的序列
     */
    public Long incr(String key)
    {

        if(StringUtil.isBlank(key))
        {
            return null;
        }

        Long ret = jedis.incr(key);

        return ret;
    }
}
