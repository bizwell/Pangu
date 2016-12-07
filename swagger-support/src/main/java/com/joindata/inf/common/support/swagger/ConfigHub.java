package com.joindata.inf.common.support.swagger;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.annotation.JoindataConfigHub;
import com.joindata.inf.common.basic.annotation.WebConfig;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.swagger.bootconfig.SwaggerConfig;

/**
 * FastDFS 支持配置器
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:30:49
 */
@JoindataConfigHub
@Configuration
@ComponentScan
@WebConfig(SwaggerConfig.class)
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}