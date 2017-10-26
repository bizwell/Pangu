package com.joindata.inf.zipkin;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import org.springframework.context.annotation.Configuration;

/**
 * zipkin 支持配置器
 *
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月7日 下午4:29:51
 */
@Configuration
@EnableDisconf
public class ConfigHub extends AbstractConfigHub {
    @Override
    protected void check() {

    }
}