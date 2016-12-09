package com.joindata.inf.common.support.elasticsearch.bootconfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.errors.SystemError;
import com.joindata.inf.common.basic.exceptions.SystemException;
import com.joindata.inf.common.support.elasticsearch.component.ElasticSearchClient;
import com.joindata.inf.common.support.elasticsearch.support.properties.ElasticSearchProperties;
import com.joindata.inf.common.util.log.Logger;

/**
 * ElasticSearch 客户端配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月7日 下午1:51:44
 */
@Configuration
public class ElasticSearchConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private ElasticSearchProperties properties;

    @Bean
    public ElasticSearchClient elasticsearchClient()
    {
        if(properties == null)
        {
            throw new SystemException(SystemError.DEPEND_RESOURCE_NOT_READY, "没有取得 ElasticSearch 的配置");
        }

        Settings settings = Settings.builder().put("cluster.name", properties.getClusterName()).build();

        ElasticSearchClient client = new ElasticSearchClient(settings);

        String[] hosts = properties.getHosts().split(",");

        try
        {
            for(String host: hosts)
            {
                String[] pair = host.split(":");
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(pair[0]), Integer.parseInt(pair[1])));
            }
        }
        catch(UnknownHostException e)
        {
            throw new SystemException(SystemError.DEPEND_RESOURCE_CANNOT_READY, "地址不对：" + e.getMessage());
        }

        log.info("ElasticSearch 连接主机: {}, 集群名称: {}", properties.getHosts(), properties.getClusterName());

        return client;
    }
}