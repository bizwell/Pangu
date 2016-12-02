package com.joindata.inf.common.support.fastdfs;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.annotation.JoindataConfigHub;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.fastdfs.bootconfig.FastDfsConfig;
import com.joindata.inf.common.support.fastdfs.bootconfig.WebMvcConfig;

/**
 * FastDFS 支持配置器
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:30:49
 */
@JoindataConfigHub
@Configuration
@ComponentScan
@EnableDisconf
@Import(value = {FastDfsConfig.class, WebMvcConfig.class})
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class, DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}