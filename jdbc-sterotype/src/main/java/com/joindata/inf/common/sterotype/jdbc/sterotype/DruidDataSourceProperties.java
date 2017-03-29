package com.joindata.inf.common.sterotype.jdbc.sterotype;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Druid 数据源参数
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 12:13:32 PM
 */
public abstract class DruidDataSourceProperties
{
    protected String name;

    /** URL */
    protected String url;

    /** 用户名 */
    protected String username;

    /** 密码 */
    protected String password;

    /** 初始化大小 */
    protected int initialSize;

    /** 最小空闲数 */
    protected int minIdle;

    /** 最大连接数 */
    protected int maxActive;

    /** 连接等待超时时间 */
    protected long maxWait;

    /** 检测关闭空闲连接的间隔时间毫秒数 */
    protected int timeBetweenEvictionRunsMillis;

    /** 最小生存时间毫秒数 */
    protected int minEvictableIdleTimeMillis;

    /** 验证语句 */
    protected String validationQuery;

    /** 是否在空闲时检测 */
    protected boolean testWhileIdle;

    /** 是否在取得时检测 */
    protected boolean testOnBorrow;

    /** 是否在归还时检测 */
    protected boolean testOnReturn;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getInitialSize()
    {
        return initialSize;
    }

    public void setInitialSize(int initialSize)
    {
        this.initialSize = initialSize;
    }

    public int getMinIdle()
    {
        return minIdle;
    }

    public void setMinIdle(int minIdle)
    {
        this.minIdle = minIdle;
    }

    public int getMaxActive()
    {
        return maxActive;
    }

    public void setMaxActive(int maxActive)
    {
        this.maxActive = maxActive;
    }

    public long getMaxWait()
    {
        return maxWait;
    }

    public void setMaxWait(long maxWait)
    {
        this.maxWait = maxWait;
    }

    public int getTimeBetweenEvictionRunsMillis()
    {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis()
    {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis)
    {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery()
    {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery)
    {
        this.validationQuery = validationQuery;
    }

    public boolean isTestWhileIdle()
    {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle)
    {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow()
    {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn()
    {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn)
    {
        this.testOnReturn = testOnReturn;
    }

    public DruidDataSource toDataSource()
    {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(this.getUrl());
        ds.setUsername(this.getUsername());
        ds.setPassword(this.getPassword());
        ds.setInitialSize(this.getInitialSize());
        ds.setMinIdle(this.getMinIdle());
        ds.setMaxActive(this.getMaxActive());
        ds.setMaxWait(this.getMaxWait());
        ds.setTimeBetweenEvictionRunsMillis(this.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(this.getMinEvictableIdleTimeMillis());
        ds.setValidationQuery(this.getValidationQuery());
        ds.setTestWhileIdle(this.isTestWhileIdle());
        ds.setTestOnBorrow(this.isTestOnBorrow());
        ds.setTestOnReturn(this.isTestOnReturn());

        return ds;
    }

}
