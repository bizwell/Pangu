package com.joindata.inf.common.util.network.entity;

/**
 * 主机端口对
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 22, 2016 11:54:44 AM
 */
public class HostPort
{
    private String host;

    private int port;

    public HostPort(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    @Override
    public String toString()
    {
        return this.getHost() + ":" + this.getPort();
    }

}
