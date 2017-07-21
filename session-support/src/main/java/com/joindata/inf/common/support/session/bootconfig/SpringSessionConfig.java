package com.joindata.inf.common.support.session.bootconfig;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.joindata.inf.common.util.log.Logger;

/**
 * SpringSession 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 22, 2016 12:06:03 PM
 */
@Configuration
@EnableRedisHttpSession
public class SpringSessionConfig
{
    private static final Logger log = Logger.get();

    @PostConstruct
    public void log()
    {
        log.info("已启用 Redis 管理的 HttpSession");
    }
}
