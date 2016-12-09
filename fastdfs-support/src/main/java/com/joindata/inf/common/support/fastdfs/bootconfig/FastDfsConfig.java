package com.joindata.inf.common.support.fastdfs.bootconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient.Builder;
import com.joindata.inf.common.support.fastdfs.dependency.client.TrackerServer;
import com.joindata.inf.common.support.fastdfs.support.properties.FastDfsProperties;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

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
    public FastdfsClient buildClient()
    {
        Builder builder = FastdfsClient.newBuilder();
        builder.connectTimeout(properties.getConnect_timeout());
        builder.readTimeout(properties.getRead_timeout());

        List<TrackerServer> serverList = CollectionUtil.newList();
        for(String serverStr: StringUtil.splitToArray(properties.getTracker_server()))
        {
            String serverHostPort[] = serverStr.split(":");
            serverList.add(new TrackerServer(serverHostPort[0], Integer.parseInt(serverHostPort[1])));
        }

        builder.trackers(serverList);

        log.info("FastDFS Tracker 地址: {}", properties.getTracker_server());

        return builder.build();
    }
}