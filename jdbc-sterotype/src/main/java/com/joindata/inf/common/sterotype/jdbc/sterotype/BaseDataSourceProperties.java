package com.joindata.inf.common.sterotype.jdbc.sterotype;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Druid 数据源参数
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 12:13:32 PM
 */
public class BaseDataSourceProperties
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
    protected long timeBetweenEvictionRunsMillis;

    /** 最小生存时间毫秒数 */
    protected long minEvictableIdleTimeMillis;

    /** 验证语句 */
    protected String validationQuery;

    /** 是否在空闲时检测 */
    protected boolean testWhileIdle;

    /** 是否在取得时检测 */
    protected boolean testOnBorrow;

    /** 是否在归还时检测 */
    protected boolean testOnReturn;

    private SlaveDataSourceProperties[] slaves;

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

    public long getTimeBetweenEvictionRunsMillis()
    {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis()
    {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis)
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

    public SlaveDataSourceProperties[] getSlaves()
    {
        return slaves;
    }

    public void setSlaves(SlaveDataSourceProperties[] slaves)
    {
        this.slaves = slaves;
    }

    public DataSource toDataSource()
    {
        DruidDataSource ds = new DruidDataSource();
        ds.setName(this.getName());
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

    public static BaseDataSourceProperties of(DruidDataSource ds)
    {
        BaseDataSourceProperties props = new BaseDataSourceProperties();
        props.setName(ds.getName());
        props.setUrl(ds.getUrl());
        props.setUsername(ds.getUsername());
        props.setPassword(ds.getPassword());
        props.setInitialSize(ds.getInitialSize());
        props.setMinIdle(ds.getMinIdle());
        props.setMaxActive(ds.getMaxActive());
        props.setMaxWait(ds.getMaxWait());
        props.setTimeBetweenEvictionRunsMillis(ds.getTimeBetweenEvictionRunsMillis());
        props.setMinEvictableIdleTimeMillis(ds.getMinEvictableIdleTimeMillis());
        props.setValidationQuery(ds.getValidationQuery());
        props.setTestWhileIdle(ds.isTestWhileIdle());
        props.setTestOnBorrow(ds.isTestOnBorrow());
        props.setTestOnReturn(ds.isTestOnReturn());

        return props;
    }
}
