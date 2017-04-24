package com.joindata.inf.common.basic.support;

import java.lang.annotation.Annotation;
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

    private static Set<Class<? extends AbstractConfigHub>> CONFIG_HUBS_CLASSES = new HashSet<>();

    private static Set<AbstractConfigHub> CONFIG_HUBS = new HashSet<>();

    private static final Map<String, Object> bootInfoMap = new HashMap<>();

    private static String APP_ID = "";

    private static String APP_VERSION = "";

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
     * 设置 AppId
     * 
     * @param appId AppId
     */
    public static void setAppId(String appId)
    {
        APP_ID = appId;
    }

    /**
     * 获取 APP_ID
     * 
     * @return AppID
     */
    public static String getAppId()
    {
        return APP_ID;
    }

    /**
     * 设置应用版本号
     * 
     * @param appVersion 应用版本号
     */
    public static void setAppVersion(String appVersion)
    {
        APP_VERSION = appVersion;
    }

    /**
     * 获取应用版本号
     * 
     * @return 应用版本号
     */
    public static String getAppVersion()
    {
        return APP_VERSION;
    }

    /**
     * 获取启动类是否有某个注解<br />
     * <i>不懂就<strong>不要瞎调</strong></i><br />
     * <i>这个方法主要用于检查组件依赖</i>
     * 
     * @return 启动类 Class
     */
    public static boolean hasBootAnno(Class<? extends Annotation> annoClz)
    {
        return BOOT_CLASS.getAnnotation(annoClz) != null;
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
    @SuppressWarnings("unchecked")
    public static void addConfigHubs(Set<Class<?>> clzSet)
    {

        for(Class<?> clz: clzSet)
        {
            CONFIG_HUBS_CLASSES.add((Class<? extends AbstractConfigHub>)clz);

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
    public static Set<Class<? extends AbstractConfigHub>> getConfigHubClasses()
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

    /**
     * 获取应用的顶级包<br />
     * <i>返回启动类所在包</i>
     * 
     * @return 包名
     */
    public static String getAppPackage()
    {
        return BOOT_CLASS.getPackage().getName();
    }

    /**
     * 环境相关的操作
     * 
     * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
     * @date Apr 20, 2017 10:21:24 AM
     */
    public static final class Env
    {
        private static String ENV = System.getProperty("disconf.env", "LOCAL");

        public static final String get()
        {
            return ENV;
        }

        public static final boolean isLOCAL()
        {
            return ENV.equals("LOCAL");
        }

        public static final boolean isDEV()
        {
            return ENV.equals("DEV");
        }

        public static final boolean isTEST()
        {
            return ENV.equals("TEST");
        }

        public static final boolean isUAT()
        {
            return ENV.equals("UAT");
        }

        public static final boolean isDRILL()
        {
            return ENV.equals("DRILL");
        }

        public static final boolean isPROD()
        {
            return ENV.equals("PROD");
        }
    }
}
