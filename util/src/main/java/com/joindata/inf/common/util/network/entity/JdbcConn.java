package com.joindata.inf.common.util.network.entity;

import com.joindata.inf.common.basic.entities.StringMap;

/**
 * JDBC 连接参数
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 5:16:02 PM
 */
public class JdbcConn
{
    private String dbType;

    private HostPort hostPort;

    private String dbName;

    private StringMap params;

    public String getDbType()
    {
        return dbType;
    }

    public void setDbType(String dbType)
    {
        this.dbType = dbType;
    }

    public HostPort getHostPort()
    {
        return hostPort;
    }

    public void setHostPort(HostPort hostPort)
    {
        this.hostPort = hostPort;
    }

    public String getDbName()
    {
        return dbName;
    }

    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }

    public StringMap getParams()
    {
        return params;
    }

    public void setParams(StringMap params)
    {
        this.params = params;
    }

    @Override
    public String toString()
    {
        return "jdbc: " + this.dbType + ", HostPort: " + this.hostPort + ", DB: " + this.dbName + ", Param: " + this.params;
    }
}
