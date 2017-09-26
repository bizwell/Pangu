package com.joindata.inf.common.support.mybatis.support.plugin;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;

import com.github.pagehelper.PageHelper;
import com.joindata.inf.common.support.mybatis.annotation.MyBatisPlugin;
import com.joindata.inf.common.support.mybatis.sterotype.PluginTemplate;

@MyBatisPlugin
public class PageHelpPluginConfig implements PluginTemplate
{
    @Override
    public Interceptor instantPlugin()
    {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.put("dialect", "mysql");
        // com.github.pagehelper.SqlUtil -> setProperties
        properties.put("pageSizeZero", "true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}