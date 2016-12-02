package com.joindata.inf.common.support.fastdfs.support.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * FastDFS 配置变量
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月1日 下午4:41:58
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "fastdfs.properties")
public class FastDfsProperties
{
    private long connect_timeout;

    private long read_timeout;

    private String tracker_server;

    @DisconfFileItem(name = "fdfs.connect_timeout", associateField = "connect_timeout")
    public long getConnect_timeout()
    {
        return connect_timeout;
    }

    public void setConnect_timeout(long connect_timeout)
    {
        this.connect_timeout = connect_timeout;
    }

    @DisconfFileItem(name = "fdfs.read_timeout", associateField = "read_timeout")
    public long getRead_timeout()
    {
        return read_timeout;
    }

    public void setRead_timeout(long read_timeout)
    {
        this.read_timeout = read_timeout;
    }

    @DisconfFileItem(name = "fdfs.tracker_server", associateField = "tracker_server")
    public String getTracker_server()
    {
        return tracker_server;
    }

    public void setTracker_server(String tracker_server)
    {
        this.tracker_server = tracker_server;
    }

}