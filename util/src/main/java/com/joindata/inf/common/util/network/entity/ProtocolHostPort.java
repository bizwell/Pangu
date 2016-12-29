package com.joindata.inf.common.util.network.entity;

/**
 * 带协议的主机端口对
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 29, 2016 10:25:56 AM
 */
public class ProtocolHostPort extends HostPort
{
    /** 协议 */
    private String protocol;

    public ProtocolHostPort(String protocol, String host, int port)
    {
        super(host, port);
        this.protocol = protocol;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    @Override
    public String toString()
    {
        return this.protocol + "//" + super.toString();
    }

}
