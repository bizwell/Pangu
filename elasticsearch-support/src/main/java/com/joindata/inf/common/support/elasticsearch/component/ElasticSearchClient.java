package com.joindata.inf.common.support.elasticsearch.component;

import java.util.Arrays;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * ElasticSearch 客户端的包装
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月7日 下午1:59:17
 */
public class ElasticSearchClient extends PreBuiltTransportClient
{

    @SafeVarargs
    public ElasticSearchClient(Settings settings, Class<? extends Plugin>... plugins)
    {
        super(settings, Arrays.asList(plugins));
    }

}
