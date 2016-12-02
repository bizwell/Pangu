package com.joindata.inf.common.support.fastdfs.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient.Builder;
import com.joindata.inf.common.support.fastdfs.dependency.client.TrackerServer;
import com.joindata.inf.common.support.fastdfs.support.properties.FastDfsProperties;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;

/**
 * FastDFS 客户端的持有者
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:26:22
 */
@Component
public class FastDfsClientHolder
{
    @Autowired
    private FastDfsProperties properties;

    private static FastdfsClient client;

    /**
     * 获取客户端
     * 
     * @return 客户端
     */
    public FastdfsClient getClient()
    {
        if(client != null)
        {
            return client;
        }

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
        return builder.build();
    }
}
