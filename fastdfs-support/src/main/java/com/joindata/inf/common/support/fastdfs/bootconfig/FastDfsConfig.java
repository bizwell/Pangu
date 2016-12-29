package com.joindata.inf.common.support.fastdfs.bootconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.fastdfs.dependency.client.AbstractFastDfsClient;
import com.joindata.inf.common.support.fastdfs.dependency.client.AbstractFastDfsClient.Builder;
import com.joindata.inf.common.support.fastdfs.dependency.client.TrackerServer;
import com.joindata.inf.common.support.fastdfs.support.component.FastDfsClient;
import com.joindata.inf.common.support.fastdfs.support.properties.FastDfsProperties;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.HostPort;

/**
 * 配置 FastDFS
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:29:31
 */
@Configuration
public class FastDfsConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private FastDfsProperties properties;

    @Bean
    public FastDfsClient buildClient()
    {
        Builder builder = AbstractFastDfsClient.newBuilder();
        builder.connectTimeout(properties.getConnect_timeout());
        builder.readTimeout(properties.getRead_timeout());

        HostPort[] hostPorts = NetworkUtil.parseHostPort(properties.getTracker_server());

        List<TrackerServer> serverList = CollectionUtil.newList();
        for(HostPort hostPort: hostPorts)
        {
            serverList.add(new TrackerServer(hostPort.getHost(), hostPort.getPort()));
        }

        builder.trackers(serverList);

        log.info("FastDFS Tracker 地址: {}", properties.getTracker_server());

        return new FastDfsClient(builder);
    }
}