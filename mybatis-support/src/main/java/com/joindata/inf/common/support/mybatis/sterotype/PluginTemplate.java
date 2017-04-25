package com.joindata.inf.common.support.mybatis.sterotype;

import org.apache.ibatis.plugin.Interceptor;

/**
 * 插件模板
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 25, 2017 2:05:31 PM
 */
public interface PluginTemplate
{
    Interceptor instantPlugin();
}
