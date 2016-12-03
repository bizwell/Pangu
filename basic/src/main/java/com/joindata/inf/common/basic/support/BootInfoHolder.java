package com.joindata.inf.common.basic.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;

/**
 * 一些启动信息的持有者
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午1:25:51
 */
public class BootInfoHolder
{
    private static Class<?> BOOT_CLASS = null;

    private static Set<Class<?>> CONFIG_HUBS_CLASSES = new HashSet<>();

    private static Set<AbstractConfigHub> CONFIG_HUBS = new HashSet<>();

    private static final Map<String, Object> bootInfoMap = new HashMap<>();

    /**
     * 设置启动信息
     * 
     * @param name 名字
     * @param value 内容
     */
    public static final void put(String name, Object value)
    {
        bootInfoMap.put(name, value);
    }

    /**
     * 取得启动信息
     * 
     * @param name 名字
     * @param clz 启动信息类型
     * @return 取得的启动信息
     */
    @SuppressWarnings("unchecked")
    public static final <T> T get(String name, Class<T> clz)
    {
        return (T)bootInfoMap.get(name);
    }

    /**
     * 取得启动信息字符串
     * 
     * @param name 名字
     * @return 取得的启动信息
     */
    public static final String get(String name)
    {
        if(bootInfoMap.get(name) == null)
            return null;
        else
            return bootInfoMap.get(name).toString();
    }

    /**
     * 设置启动类<br />
     * <i>不懂就<strong>不要瞎调</strong></i>
     * 
     * @param clz 启动类 Class
     */
    public static void setBootClass(Class<?> clz)
    {
        BOOT_CLASS = clz;
    }

    /**
     * 获取启动类<br />
     * <i>不懂就<strong>不要瞎调</strong></i>
     * 
     * @return 启动类 Class
     */
    public static Class<?> getBootClass()
    {
        return BOOT_CLASS;
    }

    /**
     * 添加配置器类<br />
     * <i>不懂就<strong>不要瞎调</strong></i>
     * 
     * @param clz 配置器类
     */
    public static void addConfigHub(Class<? extends AbstractConfigHub> clz)
    {
        CONFIG_HUBS_CLASSES.add(clz);

        try
        {
            CONFIG_HUBS.add(clz.newInstance());
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * 添加多个配置器类<br />
     * <i>不懂就<strong>不要瞎调</strong></i>
     * 
     * @param clzSet 配置器类集合
     */
    public static void addConfigHubs(Set<Class<?>> clzSet)
    {
        CONFIG_HUBS_CLASSES.addAll(clzSet);

        for(Class<?> clz: clzSet)
        {
            try
            {
                CONFIG_HUBS.add((AbstractConfigHub)clz.newInstance());
            }
            catch(InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /**
     * 获取所有配置器类Class<br />
     * <i>不懂就<strong>不要瞎调</strong></i>
     * 
     * @return 配置器类集合
     */
    public static Set<Class<?>> getConfigHubClasses()
    {
        return CONFIG_HUBS_CLASSES;
    }

    /**
     * 获取所有配置器类<br />
     * <i>不懂就<strong>不要瞎调</strong></i>
     * 
     * @return 配置器类集合
     */
    public static Set<AbstractConfigHub> getConfigHubs()
    {
        return CONFIG_HUBS;
    }

}
